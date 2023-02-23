package com.xlt.utils.common;

import com.alibaba.fastjson.JSON;
import com.xlt.model.vo.RoleVo;
import com.xlt.model.vo.UserInfoVo;
import com.xlt.model.vo.UserRoleVo;
import com.xlt.model.vo.UserVo;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    private static final Mapper dozerMapper = new DozerBeanMapper();

    public static <T> Map<String,Object> getAllFields(T obj){
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        Map<String,Object> fieldMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.get(obj)!=null) {
                    fieldMap.put(field.getName(),field.get(obj));
                } else {
                    fieldMap.put(field.getName(),"null");
                }
                logger.debug("属性名:" + field.getName() + " 属性值:" + field.get(obj));
            } catch (IllegalAccessException e) {
                logger.error("get field error:"+e);
            }
        }
        return fieldMap;
    }

    public static <T> Map<String,Object> getNonNullFields(T obj){
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        Map<String,Object> fieldMap = new HashMap<>();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.get(obj)!=null) {
                    fieldMap.put(field.getName(),field.get(obj));
                    logger.debug("属性名:" + field.getName() + " 属性值:" + field.get(obj));
                }
            } catch (IllegalAccessException e) {
                logger.error("get field error:"+e);
            }
        }
        return fieldMap;
    }

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
