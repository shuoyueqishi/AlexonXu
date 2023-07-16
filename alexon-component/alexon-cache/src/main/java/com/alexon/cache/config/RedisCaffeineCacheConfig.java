package com.alexon.cache.config;


import com.alexon.cache.enums.CacheTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCaffeineCacheConfig implements Serializable {
    private static final long serialVersionUID = -3435890018095422827L;

    private String name;

    private CacheTypeEnum type;

    private Long l1MaxSize;

    private Long l1Ttl;

    private Long l2Ttl;

    private Long l2IdleTime;

    private Boolean allowValueNull;
}
