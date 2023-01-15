package com.xlt.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@RefreshScope
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
//        Config config = Config.fromYAML(new ClassPathResource("redissonSingle.yml").getInputStream());
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        return Redisson.create(config);
    }

}
