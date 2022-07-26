package com.xlt.logs;

import com.alibaba.fastjson.JSON;
import com.xlt.mapper.IOperationLogMapper;
import com.xlt.model.po.OperationLogPo;
import com.xlt.utils.common.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    @Autowired
    IOperationLogMapper operationLogMapper;

    /**
     * 设置操作日志切入点   在注解的位置切入代码
     */
    @Pointcut("@annotation(com.xlt.logs.OperationLog)")
    public void operateLogPointCut() {
    }

    /**
     * 记录操作日志
     *
     * @param joinPoint 方法的执行点
     * @param result    方法返回值
     * @throws Throwable
     */
    @AfterReturning(returning = "result", value = "operateLogPointCut()")
    public void saveOperLog(JoinPoint joinPoint, Object result) throws Throwable {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            //获取切入点所在的方法
            Method method = signature.getMethod();

            //获取操作
            OperationLogPo operationLogPo = new OperationLogPo();
            OperationLog annotation = method.getAnnotation(OperationLog.class);
            if (annotation != null) {
                operationLogPo.setOperateModule(annotation.operateModule());
                operationLogPo.setOperateType(annotation.operateType());
                operationLogPo.setOperateDesc(annotation.operateDesc());
            }
            //操作时间
            operationLogPo.setCreationTime(new Date());

            //操作用户
            operationLogPo.setUserName(request.getHeader("userName"));
            //操作IP
            operationLogPo.setUserIp(IpUtil.getIpAddr(request));

            operationLogPo.setUrl(request.getRequestURI());

            // 请求参数
            Map<String, Object> requestParams = getRequestParams(joinPoint);
            operationLogPo.setRequest(JSON.toJSONString(requestParams));

            // 返回值
            operationLogPo.setResponse(JSON.toJSONString(result));

            //保存日志
            operationLogMapper.insert(operationLogPo);

        } catch (Exception e) {
            log.error("error while record operation log:", e);
        }
    }

    /**
     * 获取参数列表
     *
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private Map<String, Object> getRequestParams(JoinPoint joinPoint) {
        // 参数值
        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        Map<String, Object> paramMap = new HashMap<>(32);
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }
        return paramMap;
    }

}

