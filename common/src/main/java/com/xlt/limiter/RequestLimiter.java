package com.xlt.limiter;

import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

public @interface RequestLimiter {
    String limiterName() default "";
    RateType mode() default RateType.PER_CLIENT;
    long rate() default 1;
    long rateInterval() default 1;
    RateIntervalUnit rateIntervalUnit() default RateIntervalUnit.SECONDS;
}
