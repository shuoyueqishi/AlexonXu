package com.xlt.exception;

public enum ErrorEnum {
    // 数据操作错误定义
    SUCCESS("200", "access to resource success"),
    NO_PERMISSION("403","you have no suitable permission"),
    NO_AUTH("401","no authorization,please check"),
    NOT_FOUND("404", "resource not found"),
    BAD_ARGUMENT_ERROR("400", "bad argument, please check."),
    API_TOKEN_ERROR("405", "API token is wrong."),
    INTERNAL_SERVER_ERROR("500", "server internal error"),
    REPETITIVE_REQUEST_ERROR("501", "repetitive request, please don't submit again."),
    PARAMETER_ERROR("502", "Parameter is wrong."),
    ;
    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
