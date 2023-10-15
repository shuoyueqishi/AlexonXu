package com.alexon.cache.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CacheMgtRequest implements Serializable {

    private static final long serialVersionUID = 9156378920142429829L;

    private String cacheName;
    private String cacheKey;
}
