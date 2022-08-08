package com.xlt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xlt.model.po.OrderCommodityPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IOrderCommodityMapper extends BaseMapper<OrderCommodityPo> {
    int batchInsert(@Param("list") List<OrderCommodityPo> list);
}