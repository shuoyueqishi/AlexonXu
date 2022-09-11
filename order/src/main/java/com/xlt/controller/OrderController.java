package com.xlt.controller;

import com.xlt.auth.OperatePermission;
import com.xlt.constant.OperateConstant;
import com.xlt.limiter.RequestLimiter;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "OrderController",operateCode = OperateConstant.CREATE, operateDesc = "create order")
    @RequestLimiter(limiterName = "createOrder", rate = 20)
    BasicResponse createOrder(@RequestBody OrderVo orderVo) {
        return orderService.createOrder(orderVo);
    }

    @RequestMapping(value = "/create/async", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "OrderController",operateCode = OperateConstant.CREATE, operateDesc = "asynchronously create order")
    @RequestLimiter(limiterName = "asyncCreateOrder", rate = 40)
    BasicResponse asyncCreateOrder(@RequestBody OrderVo orderVo) {
        return orderService.asyncCreateOrder(orderVo);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.DELETE, operateDesc = "delete order")
    BasicResponse deleteOrder(@PathVariable("orderId") Long id) {
        return orderService.deleteOrder(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.UPDATE, operateDesc = "update order")
    BasicResponse updateOrder(@RequestBody OrderVo orderVo) {
        return orderService.updateOrder(orderVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @OperatePermission(resourceName = "OrderController",operateCode =OperateConstant.READ, operateDesc = "query order list")
    DataResponse<List<OrderVo>> queryOrderList(@QueryParam("orderVo") OrderVo orderVo) {
        return orderService.queryOrderList(orderVo);
    }
}

