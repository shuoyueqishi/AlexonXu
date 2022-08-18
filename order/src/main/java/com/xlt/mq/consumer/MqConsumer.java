package com.xlt.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xlt.model.vo.OrderVo;
import com.xlt.service.impl.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MqConsumer {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedissonClient redissonClient;

    @RabbitListener(queues = "orderQueue")
    @RabbitHandler
    void orderConsume(Message message, @Headers Map<String,Object> headers, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        if(StringUtils.isEmpty(messageId)) {
            ackSuccess(headers,channel);
            return;
        }
        log.info("receive messageId={}",messageId);
        RLock fairLock = redissonClient.getFairLock("lockMessageId:" + messageId);
        try {
            if(fairLock.tryLock(10,9, TimeUnit.SECONDS)) {
                OrderVo orderVo = JSON.parseObject(message.getBody(), OrderVo.class);
                orderService.createRealOrder(orderVo);
                log.info("asynchronously create order success");
                ackSuccess(headers,channel);
            }
        } catch (InterruptedException e) {
            log.error("process mq msg error:",e);
            ackFail(headers,channel);
        } finally {
            if (fairLock.isHeldByCurrentThread()) {
                fairLock.unlock();
            }
        }
    }

    private void ackFail(@Header Map<String,Object> headers,Channel channel) throws IOException {
        long deliveryTag = (long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicNack(deliveryTag,false,true);
    }

    private void ackSuccess(@Header Map<String,Object> headers,Channel channel) throws IOException {
        long deliveryTag = (long)headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag,true);
    }
}
