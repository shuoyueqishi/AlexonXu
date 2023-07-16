package com.alexon.cache.enums;



public enum SyncCacheActionEnum {
    UPDATE("Update"),
    EVICT("Evict"),
    CLEAR("Clear");

    private String action;
    SyncCacheActionEnum(String action) {
        this.action = action;
    }

    public String getAction(){
        return action;
    }
}
