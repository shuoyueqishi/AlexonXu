package com.xlt.idempotent;

import java.lang.annotation.*;

/**
 * 幂等注解
 * @author wangchao
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 幂等名称，作为redis缓存Key的一部分。
     */
    String value();

    /**
     * 幂等过期时间，即：在此时间段内，对API进行幂等处理。
     */
    long expireMillis();
}

