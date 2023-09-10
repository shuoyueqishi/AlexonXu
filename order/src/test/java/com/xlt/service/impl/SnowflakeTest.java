package com.xlt.service.impl;

import com.alexon.authorization.utils.PoUtil;
import com.alexon.distributed.id.SnowflakeIdGenerator;
import com.alexon.distributed.id.mapper.ISnowflakeWorkerIdMapper;
import com.alexon.distributed.id.po.SnowflakeWorkerIdPo;
import com.alexon.exception.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
@Slf4j
public class SnowflakeTest {

    @Autowired
    private ISnowflakeWorkerIdMapper snowflakeWorkerIdMapper;

    @Test
    public void workerIdIncrTest() {
        Long prevId = null;
        for (int i = 0; i < 3; i++) {
            SnowflakeWorkerIdPo po = new SnowflakeWorkerIdPo();
            PoUtil.buildCreateUserInfo(po);
            snowflakeWorkerIdMapper.insert(po);
            Long curId = po.getId();
            log.info("curId={}", curId);
            if (Objects.nonNull(prevId)) {
                AssertUtil.isTrue(curId <= prevId, "Id not increment");
            }
            prevId = curId;
        }
    }

    @Test
    public void generateDistId(){
        for(int i=0;i<100;i++) {
            Long nextId = SnowflakeIdGenerator.getInstance().nextId();
            log.info("nextId={}",nextId);
        }
    }

}
