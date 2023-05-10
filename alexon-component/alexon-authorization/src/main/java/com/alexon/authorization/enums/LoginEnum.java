package com.alexon.authorization.enums;

/**
 * 登录类型枚举
 */
public enum LoginEnum {
    WECHAT("wechat"),
    WX_MINI_PROGRAM("wxMiniProgram"),
    NORMAL("normal"),
    EMAIL("email"),
    TELEPHONE("telephone");

    private String type;

    LoginEnum(String type) {
        this.type=type;
    }

    public String getType(){
        return type;
    }
}
