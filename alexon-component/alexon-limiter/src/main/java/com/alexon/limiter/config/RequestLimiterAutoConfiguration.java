package com.alexon.limiter.config;


import com.alexon.limiter.RequestLimiterInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@ConditionalOnClass(RedissonClient.class)
@Slf4j
public class RequestLimiterAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private RequestLimiterInterceptor requestLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimiterInterceptor).addPathPatterns("/**");
        log.info("add requestLimiterInterceptor successfully");
    }
}
