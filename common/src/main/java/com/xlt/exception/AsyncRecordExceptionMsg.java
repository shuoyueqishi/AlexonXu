package com.xlt.exception;

import com.xlt.mapper.IExceptionMsgMapper;
import com.xlt.model.po.ExceptionMsgPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncRecordExceptionMsg {

    @Autowired
    private IExceptionMsgMapper exceptionMsgMapper;

    @Async("asyncPoolTaskExecutor")
    public void recordExceptionMsgTask(ExceptionMsgPo exceptionMsgPo){
        log.info("begin to do recordExceptionMsgTask");
        exceptionMsgMapper.insert(exceptionMsgPo);
        log.info("end of recordExceptionMsgTask");
    }
}

