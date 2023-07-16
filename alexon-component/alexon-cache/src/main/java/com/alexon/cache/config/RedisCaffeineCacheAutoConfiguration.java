package com.alexon.cache.config;


import com.alexon.cache.RcCacheKeyGenerator;
import com.alexon.cache.RedisCaffeineCacheManager;
import com.alexon.cache.constants.CacheConstant;
import com.alexon.cache.pubsub.SyncCacheListener;
import com.alexon.cache.pubsub.SyncCacheMessage;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(RedisCaffeineCacheProperties.class)
@Slf4j
public class RedisCaffeineCacheAutoConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = {"redissonClient"})
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public RedisCaffeineCacheManager redisCaffeineCacheManager(RedisCaffeineCacheProperties properties, RedissonClient redissonClient) {
        return new RedisCaffeineCacheManager(redissonClient, properties);
    }

    @Bean
    public SyncCacheListener syncCacheListener(RedissonClient redissonClient, RedisCaffeineCacheManager cacheManager) {
        RTopic topic = redissonClient.getTopic(CacheConstant.SYNC_CACHE_TOPIC, new JsonJacksonCodec());
        SyncCacheListener syncCacheListener = new SyncCacheListener(cacheManager);
        topic.addListener(SyncCacheMessage.class, syncCacheListener);
        log.info("success to add cache sync listener for topic=[{}]", CacheConstant.SYNC_CACHE_TOPIC);
        return syncCacheListener;
    }

    @Bean
    public RcCacheKeyGenerator rcCacheKeyGenerator(){
        return new RcCacheKeyGenerator();
    }
}
