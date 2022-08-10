package com.xlt.service;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.OrderVo;

import java.util.List;

public interface IOrderService {

    BasicResponse createOrder(OrderVo orderVo);

    DataResponse<String> asyncCreateOrder(OrderVo orderVo);

    BasicResponse deleteOrder(Long id);

    BasicResponse updateOrder(OrderVo orderVo);

    DataResponse<List<OrderVo>> queryOrderList(OrderVo orderVo);
}
