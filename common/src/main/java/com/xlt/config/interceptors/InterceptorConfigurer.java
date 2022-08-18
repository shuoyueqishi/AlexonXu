package com.xlt.config.interceptors;

import com.xlt.auth.TokenInterceptor;
import com.xlt.limiter.RequestLimiterInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class InterceptorConfigurer implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Autowired
    private RequestLimiterInterceptor requestLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
        registry.addInterceptor(requestLimiterInterceptor).addPathPatterns("/**");
        log.info("initialize addInterceptors successfully");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600);
//                .allowCredentials(true);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
