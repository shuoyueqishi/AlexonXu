package com.alexon.model.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ObjectUtil {
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
                log.debug("属性名:" + field.getName() + " 属性值:" + field.get(obj));
            } catch (IllegalAccessException e) {
                log.error("get field error:"+e);
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
                    log.debug("属性名:" + field.getName() + " 属性值:" + field.get(obj));
                }
            } catch (IllegalAccessException e) {
                log.error("get field error:"+e);
            }
        }
        return fieldMap;
    }
}
