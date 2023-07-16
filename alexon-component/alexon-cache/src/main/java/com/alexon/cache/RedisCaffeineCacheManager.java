package com.alexon.cache;


import com.alexon.cache.config.RedisCaffeineCacheConfig;
import com.alexon.cache.config.RedisCaffeineCacheProperties;
import com.alexon.cache.enums.CacheTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.*;

@Slf4j
public class RedisCaffeineCacheManager extends AbstractCacheManager {

    private RedissonClient redissonClient;

    private RedisCaffeineCacheProperties rcCacheProps;

    public RedisCaffeineCacheManager(RedissonClient redissonClient, RedisCaffeineCacheProperties properties) {
        checkProperties(properties);
        this.rcCacheProps = properties;
        this.redissonClient = redissonClient;
    }


    @Override
    protected Collection<? extends Cache> loadCaches() {
        List<RedisCaffeineCache> cacheList = new ArrayList<>();
        RedisCaffeineCache defaultCache = new RedisCaffeineCache(redissonClient, rcCacheProps.getDefaultConfig());
        cacheList.add(defaultCache);
        log.info("success to initializes RedisCaffeineCache:" + defaultCache.getName());
        Map<String, RedisCaffeineCacheConfig> cacheMap = rcCacheProps.getCacheMap();
        cacheMap.forEach((name, config) -> {
            applyDefaultConfig(name, config);
            RedisCaffeineCache rcCache = new RedisCaffeineCache(redissonClient, config);
            cacheList.add(rcCache);
            log.info("success to initializes RedisCaffeineCache:" + name);
        });
        return null;
    }

    @Override
    protected Cache getMissingCache(String name) {
        return super.getMissingCache(name);
    }

    private void applyDefaultConfig(String name, RedisCaffeineCacheConfig config) {
        RedisCaffeineCacheConfig defaultConfig = rcCacheProps.getDefaultConfig();
        if (StringUtils.isEmpty(config.getName())) {
            config.setName(name);
        }
        if (Objects.isNull(config.getAllowValueNull())) {
            config.setAllowValueNull(defaultConfig.getAllowValueNull());
        }
        if (Objects.isNull(config.getType())) {
            config.setType(defaultConfig.getType());
        }
        if (Objects.isNull(config.getL1Ttl())) {
            config.setL1Ttl(defaultConfig.getL1Ttl());
        }
        if (Objects.isNull(config.getL1MaxSize())) {
            config.setL1MaxSize(defaultConfig.getL1MaxSize());
        }
        if (Objects.isNull(config.getL2Ttl())) {
            config.setL2Ttl(defaultConfig.getL2Ttl());
        }
        if (Objects.isNull(config.getL2IdleTime())) {
            config.setL2IdleTime(defaultConfig.getL2IdleTime());
        }
    }

    private void checkProperties(RedisCaffeineCacheProperties properties) {
        StringBuilder errMsg = new StringBuilder();
        if (Objects.isNull(properties)) {
            errMsg.append("config properties can't be null.\n");
        }
        if (Objects.isNull(properties.getDefaultConfig())) {
            errMsg.append("defaultConfig can't be null\n");
        }
        RedisCaffeineCacheConfig defaultConfig = properties.getDefaultConfig();
        if (Objects.isNull(defaultConfig.getAllowValueNull())) {
            errMsg.append("defaultConfig.allowValueNull can't be null\n");
        }
        if (Objects.isNull(defaultConfig.getType()) || CacheTypeEnum.check(defaultConfig.getType().getType())) {
            errMsg.append("defaultConfig.type can't be null or illegal value.");
            errMsg.append("legal types are: ").append(Arrays.toString(CacheTypeEnum.values())).append("\n");
        }
        if (StringUtils.isEmpty(defaultConfig.getName())) {
            errMsg.append("default.name can't be empty\n");
        }
        if (Objects.isNull(defaultConfig.getL1Ttl())) {
            errMsg.append("defaultConfig.l1Ttl can't be null\n");
        }
        if (Objects.isNull(defaultConfig.getL1MaxSize())) {
            errMsg.append("defaultConfig.l1MaxSize can't be null\n");
        }
        if (Objects.isNull(defaultConfig.getL2Ttl())) {
            errMsg.append("defaultConfig.l2Ttl can't be null\n");
        }
        if (Objects.isNull(defaultConfig.getL2IdleTime())) {
            errMsg.append("defaultConfig.l2IdleTime can't be null\n");
        }
        if (errMsg.length() > 0) {
            throw new RuntimeException(errMsg.toString());
        }
    }
}
