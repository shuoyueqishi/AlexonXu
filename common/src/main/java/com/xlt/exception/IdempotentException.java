package com.xlt.exception;

/**
 * 用于专门处理幂等相关异常。
 * @author xAlex
 */
public class IdempotentException extends RuntimeException {

    public IdempotentException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

