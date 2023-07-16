package com.alexon.cache;

import com.alibaba.fastjson.JSON;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class RcCacheKeyGenerator implements KeyGenerator {
    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass().getSimpleName())
                .append(".")
                .append(method.getName())
                .append("-");
        for(int i =0;i<params.length;i++) {
            key.append(JSON.toJSONString(params[i]));
            if(i!=params.length-1) {
                key.append("#");
            }
        }
        return key.toString();
    }
}
