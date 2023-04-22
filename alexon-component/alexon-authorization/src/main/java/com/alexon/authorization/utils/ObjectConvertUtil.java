package com.alexon.authorization.utils;

import com.alexon.authorization.model.vo.RoleVo;
import com.alexon.authorization.model.vo.UserInfoVo;
import com.alexon.authorization.model.vo.UserRoleVo;
import com.alexon.authorization.model.vo.UserVo;
import com.alibaba.fastjson.JSON;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class ObjectConvertUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjectConvertUtil.class);

    private static final Mapper dozerMapper = new DozerBeanMapper();



    public static <T> T convertObjs(Object origin, Class<T> tClass) {
        return dozerMapper.map(origin, tClass);
    }

    public static  <T> List<T> convertObjsList(List<? extends Object> originList, Class<T> tClass) {
        List<T> targetList = new ArrayList<>();
        originList.forEach(origin -> {
            T target = convertObjs(origin, tClass);
            targetList.add(target);
        });
        return targetList;
    }

    public static void convertMap2UserInfoVo(Map<Object, Object> map, UserInfoVo userInfoVo) {
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
}
