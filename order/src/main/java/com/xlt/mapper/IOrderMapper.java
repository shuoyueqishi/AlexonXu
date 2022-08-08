package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.OrderPo;
import org.springframework.stereotype.Component;

@Component
public interface IOrderMapper extends BaseMapper<OrderPo> {
}

