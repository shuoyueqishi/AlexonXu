package com.alexon.cache.enums;

public enum CacheTypeEnum {

    CAFFEINE("Caffeine"),
    REDIS("Redis"),
    MIXED("Mixed");

    private String type;

    CacheTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 检验类型是否合法
     *
     * @param type type
     * @return true/false
     */
    public static boolean check(String type) {
        CacheTypeEnum[] cacheTypeEnums = CacheTypeEnum.values();
        for (int i = 0; i < cacheTypeEnums.length; i++) {
            if (cacheTypeEnums[i].getType().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
