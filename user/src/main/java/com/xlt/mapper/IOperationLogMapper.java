package com.xlt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.OperationLogPo;
import com.xlt.model.po.RolePo;
import org.springframework.stereotype.Component;


@Component
public interface IOperationLogMapper extends BaseMapper<OperationLogPo> {
}
