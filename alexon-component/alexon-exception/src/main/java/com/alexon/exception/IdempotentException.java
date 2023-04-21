package com.alexon.exception;

/**
 * 用于专门处理幂等相关异常。
 *
 * @author Alexon
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

