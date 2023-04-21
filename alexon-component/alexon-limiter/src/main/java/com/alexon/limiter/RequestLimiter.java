package com.alexon.limiter;

import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimiter {
    String limiterName() default "";
    RateType mode() default RateType.PER_CLIENT;
    long rate() default 1;
    long rateInterval() default 1;
    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.SECONDS;
}
