package com.alexon.exception.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(BaseMapper.class)
@MapperScan("com.alexon.exception.mapper")
public class ExceptionMapperAutoConfiguration {
}
