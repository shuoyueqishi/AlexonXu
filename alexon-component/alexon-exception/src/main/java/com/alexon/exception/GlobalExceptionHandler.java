package com.alexon.exception;

import com.alexon.exception.async.AsyncRecordExceptionMsg;
import com.alexon.exception.model.po.ExceptionMsgPo;
import com.alexon.model.response.BasicResponse;
import com.alexon.model.utils.AppContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = CommonException.class)
    @ResponseBody
    public BasicResponse bizExceptionHandler(CommonException e) {
        log.error("CommonException error info:", e);
        recordExceptionMsg(e);
        return BasicResponse.errorWithMsg(e.getErrorMsg());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BasicResponse exceptionHandler(Exception e) {
        log.error("Exception error info:", e);
        recordExceptionMsg(e);
        return BasicResponse.errorWithMsg(e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseBody
    public BasicResponse IllegalStateExceptionHandler(IllegalStateException e) {
        log.error("IllegalStateException error info:", e);
        recordExceptionMsg(e);
        return BasicResponse.errorWithMsg(e.getMessage());
    }


    /**
     * 处理NoSuchAlgorithmException异常
     */
    @ExceptionHandler(value = NoSuchAlgorithmException.class)
    @ResponseBody
    public BasicResponse NoSuchAlgorithmExceptionHandler(NoSuchAlgorithmException e) {
        log.error("NoSuchAlgorithmException error info:", e);
        recordExceptionMsg(e);
        return BasicResponse.errorWithMsg(e.getMessage());
    }

    /**
     * 组装异常记录信息
     */
    private <T extends Exception> void recordExceptionMsg(T ex) {
        String exStackTrace = "";
        try {
            exStackTrace = getExStackTrace(ex);
        } catch (IOException e) {
            log.error("get exception stack track info error:", e);
        }
        String message = ex.getMessage();
        if (message.length() > 1024) {
            message = message.substring(0, 1024);
        }
        ExceptionMsgPo exceptionMsgPo = ExceptionMsgPo.builder()
                .msg(message)
                .stackTrace(exStackTrace)
                .creationDate(new Date())
                .createBy(-1L)
                .build();
        AsyncRecordExceptionMsg asyncRecordExMsg = AppContextUtil.getBean(AsyncRecordExceptionMsg.class);
        // 调用异步任务入库
        asyncRecordExMsg.recordExceptionMsgTask(exceptionMsgPo);
    }

    private <T extends Exception> String getExStackTrace(T ex) throws IOException {
        //读取异常堆栈信息
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ex.printStackTrace(new PrintStream(arrayOutputStream));
        //通过字节数组转换输入输出流
        BufferedReader fr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(arrayOutputStream.toByteArray())));
        String str;
        StringBuilder exceptionSb = new StringBuilder();
        while ((str = fr.readLine()) != null) {
            exceptionSb.append(str);
            exceptionSb.append("\n");
        }
        return exceptionSb.toString();
    }
}


