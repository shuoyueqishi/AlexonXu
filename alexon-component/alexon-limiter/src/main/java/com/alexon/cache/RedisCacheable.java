package com.alexon.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheable {
    /** 第一过期时间 单位:min**/
    long firstLayerTtl() default 30L;

    /** 第一过期时间 单位:min **/
    long secondLayerTtl() default 5L;

    /** Redis key 值 **/
    String key();
}
