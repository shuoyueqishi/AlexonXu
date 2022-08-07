package com.xlt.service.impl;

import com.xlt.mapper.IOrderCommodityMapper;
import com.xlt.mapper.IOrderMapper;
import com.xlt.model.po.OrderPo;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.OrderCommodityVo;
import com.xlt.model.vo.OrderVo;
import com.xlt.service.IOrderService;
import com.xlt.utils.common.AssertUtil;
import com.xlt.utils.common.ObjectUtil;
import com.xlt.utils.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrderService implements IOrderService {

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IOrderCommodityMapper orderCommodityMapper;


    @Override
    @Transactional
    public BasicResponse createOrder(OrderVo orderVo) {
        AssertUtil.isNull(orderVo, "orderVo can't be empty");
        AssertUtil.isNull(orderVo.getUserId(), "userId can't by empty");
        AssertUtil.isNull(orderVo.getReceiverId(), "receiverId can't empty");
        AssertUtil.isNull(orderVo.getOrderAmount(), "orderAmount can't be empty");
        AssertUtil.isNull(orderVo.getDeliveryAmount(), "deliveryAmount can't be empty");
        AssertUtil.isNull(orderVo.getTotalAmount(), "totalAmount can't be empty");
        AssertUtil.isTrue(!orderVo.getTotalAmount().equals(orderVo.getOrderAmount().add(orderVo.getDeliveryAmount())),
                "orderAmount+deliveryAmount != totalAmount");
        AssertUtil.isCollectionEmpty(orderVo.getCommodityList(), "commodityList can't be empty");
        for (OrderCommodityVo orderCommodityVo : orderVo.getCommodityList()) {
            AssertUtil.isNull(orderCommodityVo.getSkuId(), "skuId can't be empty");
            AssertUtil.isTrue(Objects.isNull(orderCommodityVo.getQuantity()) || orderCommodityVo.getQuantity() < 1,
                    "quantity can't be empty or less than 1");
            // 缓存中查询库存是否充足
            Long curStock = (Long)RedisUtil.get("SkuId:" + orderCommodityVo.getSkuId());
            AssertUtil.isTrue(curStock==null||curStock<orderCommodityVo.getQuantity(),"stock is not enough");
        }
        // 先创建订单头，再创建明细
        OrderPo orderPo = ObjectUtil.convertObjs(orderVo, OrderPo.class);
        // 返回主键ID才行
        int insert = orderMapper.insert(orderPo);


        return null;
    }

    @Override
    public BasicResponse deleteOrder(Long id) {
        return null;
    }

    @Override
    public BasicResponse updateOrder(OrderVo orderVo) {
        return null;
    }

    @Override
    public DataResponse<List<OrderVo>> queryOrderList(OrderVo orderVo) {
        return null;
    }
}
