package com.xlt.config.auth;

import com.alibaba.fastjson.JSON;
import com.xlt.annotation.OperatePermission;
import com.xlt.constant.CommConstant;
import com.xlt.context.UserContext;
import com.xlt.model.vo.*;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.JwtUtil;
import com.xlt.utils.common.RedisUtil;
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
                Map<Object, Object> userInfoMap = new HashMap<>();
                if (StringUtils.isEmpty(userId) && StringUtils.isNotEmpty(token)) {
                    userId = JwtUtil.verifyToken(token).get("userId").asString();
                }
                userInfoMap = RedisUtil.hmget(CommConstant.USER_INFO_PREFIX + userId);
                convertMap2UserInfoVo(userInfoMap, userInfoVo);
                List<String> permissionList = userInfoVo.getCurPermissionList();
                AssertUtil.isTrue(CollectionUtils.isEmpty(permissionList) || !permissionList.contains(perPoint),
                        "lack of permission: " + perPoint);
                UserContext.setUserInfo(userInfoVo);
                return true;
            }
        }
        return true;
    }

    private void convertMap2UserInfoVo(Map<Object, Object> map, UserInfoVo userInfoVo) {
        if (CollectionUtils.isEmpty(map)) return;
        userInfoVo.setToken((String) map.get("token"));
        if (Objects.nonNull(map.get("curUser"))) {
            userInfoVo.setCurUser(JSON.parseObject(JSON.toJSONString(map.get("curUser")), UserVo.class));
        }
        if (Objects.nonNull(map.get("curRole"))) {
            userInfoVo.setCurRole(JSON.parseObject(JSON.toJSONString(map.get("curRole")), RoleVo.class));
        }
        if (Objects.nonNull(map.get("curPermissionList"))) {
            userInfoVo.setCurPermissionList(JSON.parseArray(JSON.toJSONString(map.get("curPermissionList")), String.class));
        }
        if (Objects.nonNull(map.get("validRoleList"))) {
            userInfoVo.setValidRoleList(JSON.parseArray(JSON.toJSONString(map.get("validRoleList")), UserRoleVo.class));
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
