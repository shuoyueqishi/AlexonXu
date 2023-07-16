package com.alexon.cache;

import com.alexon.cache.config.RedisCaffeineCacheConfig;
import com.alexon.cache.constants.CacheConstant;
import com.alexon.cache.enums.CacheTypeEnum;
import com.alexon.cache.enums.SyncCacheActionEnum;
import com.alexon.cache.pubsub.SyncCacheMessage;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RedisCaffeineCache extends AbstractValueAdaptingCache {

    private String name;

    private RedisCaffeineCacheConfig cacheConfig;

    private RedissonClient redissonClient;

    private CaffeineCache caffeineCache;

    private RedissonCache redissonCache;


    protected RedisCaffeineCache(RedissonClient redissonClient, RedisCaffeineCacheConfig cacheConfig) {
        super(cacheConfig.getAllowValueNull());
        this.cacheConfig = cacheConfig;
        this.redissonClient = redissonClient;
        this.name = cacheConfig.getName();
        long initialCap = cacheConfig.getL1MaxSize() / 4;
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheConfig.getL1Ttl(), TimeUnit.SECONDS)
                .maximumSize(cacheConfig.getL1MaxSize())
                .initialCapacity((int) initialCap)
                .build();
        this.caffeineCache = new CaffeineCache(cacheConfig.getName(), cache, cacheConfig.getAllowValueNull());
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache(cacheConfig.getName());
        CacheConfig redissonCacheConfig = new CacheConfig(cacheConfig.getL2Ttl() * 1000, cacheConfig.getL2IdleTime() * 1000);
        this.redissonCache = new RedissonCache(mapCache, redissonCacheConfig, cacheConfig.getAllowValueNull());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return caffeineCache.getNativeCache();
    }

    public CaffeineCache getCaffeineCache() {
        return caffeineCache;
    }

    public RedissonCache getRedissonCache() {
        return redissonCache;
    }

    public RedisCaffeineCacheConfig getCacheConfig() {
        return cacheConfig;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (CacheTypeEnum.CAFFEINE.getType().equals(cacheConfig.getType())) {
            return caffeineCache.get(key);
        }
        if (CacheTypeEnum.REDIS.getType().equals(cacheConfig.getType())) {
            return redissonCache.get(key);
        }
        if (CacheTypeEnum.MIXED.equals(cacheConfig.getType())) {
            ValueWrapper l1Value = caffeineCache.get(key);
            if (Objects.nonNull(l1Value)) {
                return l1Value;
            }
            ValueWrapper l2value = redissonCache.get(key);
            if (Objects.isNull(l2value)) {
                return null;
            }
            caffeineCache.put(key, l2value);
            return l2value;
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (CacheTypeEnum.CAFFEINE.getType().equals(cacheConfig.getType())) {
            return caffeineCache.get(key, valueLoader);
        }
        if (CacheTypeEnum.REDIS.getType().equals(cacheConfig.getType())) {
            return redissonCache.get(key, valueLoader);
        }
        if (CacheTypeEnum.MIXED.equals(cacheConfig.getType())) {
            T l1Value = caffeineCache.get(key, valueLoader);
            if (Objects.nonNull(l1Value)) {
                return l1Value;
            }
            T l2value = redissonCache.get(key, valueLoader);
            if (Objects.isNull(l2value)) {
                return null;
            }
            caffeineCache.put(key, l2value);
            return l2value;
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        if (CacheTypeEnum.CAFFEINE.getType().equals(cacheConfig.getType())) {
            caffeineCache.put(key, value);
        }
        if (CacheTypeEnum.REDIS.equals(cacheConfig.getType())) {
            redissonCache.put(key, value);
        }
        if (CacheTypeEnum.MIXED.equals(cacheConfig.getType())) {
            redissonCache.put(key, value);
            RTopic topic = redissonClient.getTopic(CacheConstant.SYNC_CACHE_TOPIC, new JsonJacksonCodec());
            SyncCacheMessage syncCacheMessage = SyncCacheMessage.builder()
                    .cacheName(name)
                    .key(key)
                    .value(value)
                    .action(SyncCacheActionEnum.UPDATE)
                    .build();
            topic.publish(syncCacheMessage);
        }
    }

    @Override
    public void evict(Object key) {
        if (CacheTypeEnum.CAFFEINE.getType().equals(cacheConfig.getType())) {
            caffeineCache.evictIfPresent(key);
        }
        if (CacheTypeEnum.REDIS.equals(cacheConfig.getType())) {
            redissonCache.evictIfPresent(key);
        }
        if (CacheTypeEnum.MIXED.equals(cacheConfig.getType())) {
            redissonCache.evictIfPresent(key);
            RTopic topic = redissonClient.getTopic(CacheConstant.SYNC_CACHE_TOPIC, new JsonJacksonCodec());
            SyncCacheMessage syncCacheMessage = SyncCacheMessage.builder()
                    .cacheName(name)
                    .key(key)
                    .action(SyncCacheActionEnum.EVICT)
                    .build();
            topic.publish(syncCacheMessage);
        }
    }

    @Override
    public void clear() {
        if (CacheTypeEnum.CAFFEINE.getType().equals(cacheConfig.getType())) {
            caffeineCache.clear();
        }
        if (CacheTypeEnum.REDIS.equals(cacheConfig.getType())) {
            redissonCache.clear();
        }
        if (CacheTypeEnum.MIXED.equals(cacheConfig.getType())) {
            redissonCache.clear();
            RTopic topic = redissonClient.getTopic(CacheConstant.SYNC_CACHE_TOPIC, new JsonJacksonCodec());
            SyncCacheMessage syncCacheMessage = SyncCacheMessage.builder()
                    .cacheName(name)
                    .action(SyncCacheActionEnum.CLEAR)
                    .build();
            topic.publish(syncCacheMessage);
        }
    }

    @Override
    protected Object lookup(Object key) {
        return get(key);
    }
}
