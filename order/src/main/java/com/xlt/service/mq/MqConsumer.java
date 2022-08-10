package com.xlt.service.mq;

import com.alibaba.fastjson.JSON;
import com.xlt.model.vo.OrderVo;
import com.xlt.service.impl.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MqConsumer {
    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = "orderQueue")
    @RabbitHandler
    void directHandler1(String msg) {
        log.info("orderQueue: receive message is {}", msg);
        OrderVo orderVo = JSON.parseObject(msg, OrderVo.class);
        orderService.createRealOrder(orderVo);
        log.info("asynchronously to create order success");
    }

}

