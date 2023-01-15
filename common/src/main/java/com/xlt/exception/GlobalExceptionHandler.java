package com.xlt.exception;

import com.xlt.model.response.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     *
     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public BasicResponse bizExceptionHandler(CommonException e) {
        log.error("CommonException error info:",e);
        return BasicResponse.commonError(e);
    }

    /**
     * 处理其他异常
     *
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BasicResponse exceptionHandler( Exception e) {
        log.error("Exception error info:",e);
        return BasicResponse.errorWithMsg(e.toString());
    }

    /**
     * 处理自定义异常
     *
     */
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    public BasicResponse IllegalStateExceptionHandler(IllegalStateException e) {
        log.error("IllegalStateException error info:",e);
        return BasicResponse.errorWithMsg(e.getMessage());
    }


    /**
     * 处理NoSuchAlgorithmException异常
     *
     */
    @ExceptionHandler(value = NoSuchAlgorithmException.class)
    @ResponseBody
    public BasicResponse NoSuchAlgorithmExceptionHandler(NoSuchAlgorithmException e) {
        log.error("NoSuchAlgorithmException error info:",e);
        return BasicResponse.errorWithMsg(e.getMessage());
    }
}
