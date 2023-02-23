package com.xlt.model.mapper;

import com.xlt.model.po.OrderPo;
import com.xlt.model.vo.OrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderConvertor {
    OrderConvertor INSTANCE = Mappers.getMapper( OrderConvertor.class );

    OrderVo convert2OrderVo(OrderPo orderPo);

    OrderPo convert2OrderPo(OrderVo orderVo);

    List<OrderVo> convert2OrderVos(List<OrderPo> orderPos);

    List<OrderPo> convert2OrderPos(List<OrderVo> orderVos);
}
