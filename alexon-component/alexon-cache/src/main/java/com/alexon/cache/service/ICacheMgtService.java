package com.alexon.cache.service;

import com.alexon.cache.model.request.CacheMgtRequest;

public interface ICacheMgtService {

    /**
     * 根据缓存name和key失效缓存
     *
     * @param request 请求
     */
    void evictCache(CacheMgtRequest request);

    /**
     * 根据缓存name清空所有缓存
     *
     * @param request 请求
     */
    void clearCache(CacheMgtRequest request);
}
