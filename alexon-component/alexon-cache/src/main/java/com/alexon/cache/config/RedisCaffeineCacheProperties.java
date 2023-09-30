package com.alexon.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Map;


@Data
@ConfigurationProperties(value="l2cache")
public class RedisCaffeineCacheProperties implements Serializable {

    private static final long serialVersionUID = -601660420596264376L;

    private Boolean enable;

    private RedisCaffeineCacheConfig defaultConfig;

    private Map<String, RedisCaffeineCacheConfig> cacheMap;

}
