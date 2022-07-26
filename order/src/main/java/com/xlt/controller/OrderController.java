package com.xlt.controller;

import com.xlt.model.response.BasicResponse;
import com.xlt.model.response.DataResponse;
import com.xlt.model.vo.OrderVo;
import com.xlt.model.vo.UserVo;
import com.xlt.service.interfaces.IOrderService;
import com.xlt.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    BasicResponse createOrder(@RequestBody OrderVo orderVo) {
        return orderService.createOrder(orderVo);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.DELETE, produces = "application/json")
    BasicResponse deleteOrder(@PathVariable("orderId") Long id) {
        return orderService.deleteOrder(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
    BasicResponse updateOrder(@RequestBody OrderVo orderVo) {
        return orderService.updateOrder(orderVo);
    }

    @RequestMapping(value = "/query/list", method = RequestMethod.GET, produces = "application/json")
    DataResponse<List<OrderVo>> queryOrderList(@QueryParam("orderVo") OrderVo orderVo) {
        return orderService.queryOrderList(orderVo);
    }
}

