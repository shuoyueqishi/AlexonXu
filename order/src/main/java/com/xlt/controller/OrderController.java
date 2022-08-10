package com.xlt.controller;

import com.xlt.annotation.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.OrderVo;
import com.xlt.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    @OperatePermission(resourceName = "OrderController",operateCode = OperateConstant.CREATE, operateDesc = "create order")
    BasicResponse createOrder(@RequestBody OrderVo orderVo) {
        return orderService.createOrder(orderVo);
    }

    @RequestMapping(value = "/create/async", method = RequestMethod.POST, produces = "application/json")
    @OperatePermission(resourceName = "OrderController",operateCode = OperateConstant.CREATE, operateDesc = "asynchronously create order")
    BasicResponse asyncCreateOrder(@RequestBody OrderVo orderVo) {
        return orderService.asyncCreateOrder(orderVo);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE, produces = "application/json")
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.DELETE, operateDesc = "delete order")
    BasicResponse deleteOrder(@PathVariable("orderId") Long id) {
        return orderService.deleteOrder(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.UPDATE, operateDesc = "update order")
    BasicResponse updateOrder(@RequestBody OrderVo orderVo) {
        return orderService.updateOrder(orderVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = "application/json")
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.READ, operateDesc = "query order list")
    DataResponse<List<OrderVo>> queryOrderList(@QueryParam("orderVo") OrderVo orderVo) {
        return orderService.queryOrderList(orderVo);
    }
}

