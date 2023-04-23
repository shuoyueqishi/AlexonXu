package com.xlt.mq.sender;

import com.alibaba.fastjson.JSON;
import com.alexon.limiter.utils.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    public RabbitTemplate rabbitTemplate;


    public void send(String exchange, String routingKey, Object msg) {
        log.info("send mq msg,exchange={},routingKey={},msg={}", exchange, routingKey, msg);
        Message message = preHandleMsg(msg);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        log.info("send mq msg successfully");
    }


    private Message preHandleMsg(Object msg) {
        MessageProperties properties = new MessageProperties();
        properties.setMessageId(SnowflakeIdGenerator.generateId().toString());
        byte[] msgBytes = JSON.toJSONBytes(msg);
        Message message = new Message(msgBytes, properties);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
        return message;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("Confirm: " + correlationData + ", ack=" + ack + (cause == null ? "" : ", cause: " + cause));
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("returned msg is={}", returnedMessage);
    }
}

