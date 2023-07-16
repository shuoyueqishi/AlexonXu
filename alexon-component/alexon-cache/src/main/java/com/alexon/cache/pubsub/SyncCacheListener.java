package com.alexon.cache.pubsub;

import com.alexon.cache.RedisCaffeineCache;
import com.alexon.cache.RedisCaffeineCacheManager;
import com.alexon.cache.enums.SyncCacheActionEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;
import org.springframework.cache.caffeine.CaffeineCache;

@Slf4j
public class SyncCacheListener implements MessageListener<SyncCacheMessage> {

    private RedisCaffeineCacheManager manager;

    public SyncCacheListener(RedisCaffeineCacheManager manager) {
        this.manager = manager;
    }

    /**
     * Invokes on every message in topic
     *
     * @param channel of topic
     * @param msg     topic message
     */
    @Override
    public void onMessage(CharSequence channel, SyncCacheMessage msg) {
        log.info("received message, channel={},msg ={}", channel, msg);
        RedisCaffeineCache cache = (RedisCaffeineCache) manager.getCache(msg.getCacheName());
        CaffeineCache caffeineCache = cache.getCaffeineCache();
        if (SyncCacheActionEnum.UPDATE.getAction().equals(msg.getAction())) {
            caffeineCache.put(msg.getKey(), msg.getValue());
            log.info("success to update cache for cacheName:{},key={}", msg.getCacheName(), msg.getKey());
        }
        if (SyncCacheActionEnum.EVICT.getAction().equals(msg.getAction())) {
            caffeineCache.evictIfPresent(msg.getKey());
            log.info("success to evict cache for cacheName:{},key={}", msg.getCacheName(), msg.getKey());
        }
        if (SyncCacheActionEnum.CLEAR.getAction().equals(msg.getAction())) {
            caffeineCache.clear();
            log.info("success to clear cache for cacheName:{},key={}", msg.getCacheName(), msg.getKey());
        }
    }
}