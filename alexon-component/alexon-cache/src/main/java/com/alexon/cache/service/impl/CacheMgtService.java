package com.alexon.cache.service.impl;

import com.alexon.cache.RedisCaffeineCacheManager;
import com.alexon.cache.model.request.CacheMgtRequest;
import com.alexon.cache.service.ICacheMgtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class CacheMgtService implements ICacheMgtService {

    @Autowired
    private RedisCaffeineCacheManager cacheManager;

    @Override
    public void evictCache(CacheMgtRequest request) {
        if(StringUtils.isEmpty(request.getCacheName())) {
            throw new RuntimeException("cacheName can't be empty");
        }
        if(StringUtils.isEmpty(request.getCacheKey())) {
            throw new RuntimeException("cacheKey can't be empty");
        }
        Cache cache = cacheManager.getCache(request.getCacheName());
        if(Objects.isNull(cache)) {
            log.error("cacheName:{} not exists",request.getCacheName());
            return;
        }
        cache.evict(request.getCacheKey());
        log.info("success to evict cache for name:{}, key={}",request.getCacheName(),request.getCacheKey());
    }

    @Override
    public void clearCache(CacheMgtRequest request) {
        if(StringUtils.isEmpty(request.getCacheName())) {
            throw new RuntimeException("cacheName can't be empty");
        }
        Cache cache = cacheManager.getCache(request.getCacheName());
        if(Objects.isNull(cache)) {
            log.error("cacheName:{} not exists",request.getCacheName());
            return;
        }
        cache.clear();
        log.info("success to clear cache for name:{}",request.getCacheName());
    }
}
