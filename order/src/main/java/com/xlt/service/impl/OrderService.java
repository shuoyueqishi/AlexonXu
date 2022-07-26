package com.xlt.service.impl;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.OrderProductDeliveryVo;
import com.xlt.model.vo.OrderVo;
import com.xlt.model.vo.ProductVo;
import com.xlt.service.interfaces.IOrderService;
import com.xlt.utils.common.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class OrderService implements IOrderService {

    @Override
    public BasicResponse createOrder(OrderVo orderVo) {
        AssertUtil.isNull(orderVo, "orderVo can't be empty");
        AssertUtil.isNull(orderVo.getUserId(),"userId can't by empty");
        AssertUtil.isNull(orderVo.getReceiveAddressId(),"receiveAddressId can't empty");
        AssertUtil.isNull(orderVo.getAmount(),"amount can't be empty");
        AssertUtil.isCollectionEmpty(orderVo.getProductList(), "productList can't be empty");
        for (OrderProductDeliveryVo productVo : orderVo.getProductList()) {
             AssertUtil.isNull(productVo.getProductId(),"productId can't be empty");
             AssertUtil.isTrue(Objects.isNull(productVo.getQuantity())||productVo.getQuantity()<1,
                     "quantity is empty or less than 1");
        }
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
