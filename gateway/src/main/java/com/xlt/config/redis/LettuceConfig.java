package com.xlt.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class LettuceConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 使用fastjson作为序列化器，需要引入相关依赖
        GenericFastJsonRedisSerializer vauleSerializer = new GenericFastJsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 一般redis的key为String, value为Object
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(vauleSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(vauleSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
