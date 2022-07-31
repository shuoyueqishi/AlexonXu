package com.xlt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.xlt.annotation.OperatePermission;
import com.xlt.constant.CommConstant;
import com.xlt.context.UserContext;
import com.xlt.exception.CommonException;
import com.xlt.model.vo.UserInfoVo;
import com.xlt.model.vo.UserVo;
import com.xlt.service.impl.UserService;
import com.xlt.utils.common.AppContextUtil;
import com.xlt.utils.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
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
                String perPoint = resourceName+"#"+operateCode;
                String token = request.getHeader("token");
                if (StringUtils.isEmpty(token)) {
                    throw new CommonException("Can't invoke API as token not exists in http header");
                }
                Long userId;
                try {
                    userId = JWT.decode(token).getClaims().get("userId").asLong();
                } catch (JWTDecodeException e) {
                    throw new CommonException("Resolve user token failed");
                }
                String cacheToken = (String) RedisUtil.get(CommConstant.TOKEN_PREFIX + userId);
                UserInfoVo cacheUserInfo = (UserInfoVo) RedisUtil.get(CommConstant.USER_INFO_PREFIX + userId);
                if (StringUtils.isNotEmpty(cacheToken)&&cacheToken.equals(token)) {
                    checkPermissionPoint(perPoint,cacheUserInfo);
                    UserContext.setUserInfo(cacheUserInfo);
                    return true;
                }
                UserService userService = AppContextUtil.getBean(UserService.class);
                UserVo userVo = userService.queryUserById(userId);
                if (userVo == null) {
                    throw new CommonException("User not exist");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userVo.getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                    checkPermissionPoint(perPoint,cacheUserInfo);
                    UserContext.setUserInfo(cacheUserInfo);
                } catch (JWTVerificationException e) {
                    throw new CommonException("user token is invalid");
                }
                return true;
            }
        }
        return true;
    }

    private void checkPermissionPoint(String permPoint,UserInfoVo userInfoVo) {
        if (StringUtils.isEmpty(permPoint)) {
            return;
        }
        Set<String> curPermissionSet = userInfoVo.getCurPermissionSet();
        if(!curPermissionSet.contains(permPoint)) {
            throw new CommonException("lack of permission: "+permPoint);
        }
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
