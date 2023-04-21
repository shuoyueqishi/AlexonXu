package com.alexon.authorization;

import com.alexon.authorization.config.JwtConfig;
import com.alexon.authorization.constants.CommConstant;
import com.alexon.authorization.context.UserContext;
import com.alexon.authorization.model.vo.UserInfoVo;
import com.alexon.authorization.utils.JwtUtil;
import com.alexon.authorization.utils.ObjectUtil;
import com.alexon.authorization.utils.RedisUtil;
import com.alexon.exception.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Value("${spring.application.name:Alexon}")
    private String appName;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        // 内部微服务调用传入internal-》不鉴权
        String internal = request.getHeader("internal");
        if (jwtConfig.isSkipPermission() || StringUtils.isNotEmpty(internal)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(OperatePermission.class)) {
            OperatePermission requirePermission = method.getAnnotation(OperatePermission.class);
            if (Objects.nonNull(requirePermission)) {  //需要权限
                String resourceName = requirePermission.resourceName();
                String operateCode = requirePermission.operateCode();
                String perPoint = appName + "#" + resourceName + "#" + operateCode;
                String token = request.getHeader("token");
                String userId = request.getHeader("userId");
                AssertUtil.isTrue(StringUtils.isEmpty(token) && StringUtils.isEmpty(userId),
                        "Can't invoke API as token and useId both not exist in http header");
                UserInfoVo userInfoVo = new UserInfoVo();
                if (StringUtils.isEmpty(userId) && StringUtils.isNotEmpty(token)) {
                    userId = JwtUtil.verifyToken(token).get("userId").asString();
                }
                Map<Object, Object> userInfoMap = RedisUtil.hmget(CommConstant.USER_INFO_PREFIX + userId);
                long expireSec = RedisUtil.getExpireSec(CommConstant.USER_INFO_PREFIX + userId);
                log.info("current token expireSec={}", expireSec);
                long newExpireSec = expireSec + 300; // 请求一次加5min token过期时间，最长不超过jwtConfig.getJwtExpireTime()
                if (newExpireSec > jwtConfig.getJwtExpireTime()) {
                    newExpireSec = jwtConfig.getJwtExpireTime();
                }
                RedisUtil.expire(CommConstant.USER_INFO_PREFIX + userId, newExpireSec);
                ObjectUtil.convertMap2UserInfoVo(userInfoMap, userInfoVo);
                List<String> permissionList = userInfoVo.getCurPermissionList();
                AssertUtil.isTrue(CollectionUtils.isEmpty(permissionList) || !permissionList.contains(perPoint),
                        "lack of permission: " + perPoint);
                UserContext.setUserInfo(userInfoVo);
                return true;
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }

}
