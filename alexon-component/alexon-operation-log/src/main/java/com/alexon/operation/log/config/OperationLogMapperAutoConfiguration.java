package com.alexon.operation.log.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.alexon.operation.log.mapper")
public class OperationLogMapperAutoConfiguration {
}
