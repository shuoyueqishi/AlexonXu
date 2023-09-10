package com.alexon.distributed.id.mapper;

import com.alexon.distributed.id.po.SnowflakeWorkerIdPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public interface ISnowflakeWorkerIdMapper extends BaseMapper<SnowflakeWorkerIdPo> {
}
