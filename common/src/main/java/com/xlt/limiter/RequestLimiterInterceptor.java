package com.xlt.limiter;

import com.xlt.utils.common.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Slf4j
public class RequestLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(RequestLimiter.class)) {
            RequestLimiter requestLimiter = method.getAnnotation(RequestLimiter.class);
            if (Objects.nonNull(requestLimiter)) {
                String limiterName = requestLimiter.limiterName();
                AssertUtil.isStringEmpty(limiterName, "limiterName can't be empty");
                RateType mode = requestLimiter.mode();
                long rate = requestLimiter.rate();
                long rateInterval = requestLimiter.rateInterval();
                RateIntervalUnit rateIntervalUnit = requestLimiter.rateIntervalUnit();
                RRateLimiter rRateLimiter = redissonClient.getRateLimiter(limiterName);
                rRateLimiter.setRate(mode, rate, rateInterval, rateIntervalUnit);
                if (rRateLimiter.tryAcquire()) {
                    return true;
                } else {
                    log.info("request limited....");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
