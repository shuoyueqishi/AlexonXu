package com.alexon.operation.log;

import com.alexon.operation.log.mapper.IOperationLogMapper;
import com.alexon.operation.log.model.po.OperationLogPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncRecordLog {

    @Autowired
    IOperationLogMapper operationLogMapper;

    @Async("asyncPoolTaskExecutor")
    public void recordLogTask(OperationLogPo operationLogPo){
        log.info("begin to do recordLogTask, operationLogPo={}",operationLogPo);
        operationLogMapper.insert(operationLogPo);
        log.info("end of recordLogTask");
    }
}
