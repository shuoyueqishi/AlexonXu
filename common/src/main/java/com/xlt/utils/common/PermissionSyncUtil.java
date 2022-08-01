package com.xlt.utils.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xlt.model.vo.PermissionAnnotationVo;
import com.xlt.model.vo.PermissionVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;


@Slf4j
@Component
public class PermissionSyncUtil implements ApplicationContextAware {

    private static String appName;

    private static ApplicationContext applicationContext;

    @Value("${spring.application.name}")
    private void setAppName(String appName) {
        PermissionSyncUtil.appName = appName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PermissionSyncUtil.applicationContext = applicationContext;
    }


    public static List<PermissionVo> getOperationPermissions() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        List<PermissionVo> permVoList = new ArrayList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod method = entry.getValue();
            PermissionVo permVo = new PermissionVo();
            permVo.setTenant(appName);
            ApiOperation apiOperation = method.getMethodAnnotation(ApiOperation.class);
            if(Objects.nonNull(apiOperation)) {
                permVo.setApiOperation(apiOperation.value());
            }
            PatternsRequestCondition patternsCondition = info.getPatternsCondition();
            permVo.setMethodName(method.getMethod().getDeclaringClass().getName()+"#"+method.getMethod().getName());

            for (String path : patternsCondition.getPatterns()) {
                permVo.setPath(path);
            }
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
               permVo.setHttpMethod(requestMethod.toString());
            }
            permVoList.add(permVo);
        }
        return permVoList;
    }


}


