package com.alexon.cache.pubsub;

import com.alexon.cache.enums.SyncCacheActionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SyncCacheMessage {

    private String cacheName;
    private Object key;
    private Object value;
    private SyncCacheActionEnum action;
}
