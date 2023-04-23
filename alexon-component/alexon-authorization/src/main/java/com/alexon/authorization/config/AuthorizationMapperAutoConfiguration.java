package com.alexon.authorization.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.alexon.authorization.mapper")
@Slf4j
public class AuthorizationMapperAutoConfiguration {
}
