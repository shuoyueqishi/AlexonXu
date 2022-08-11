package com.xlt.mq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectExchangeConfig {

    @Bean
    public DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange("directExchange");
        return directExchange;
    }

    @Bean
    public Queue directQueue1() {
        Queue queue = new Queue("orderQueue");
        return queue;
    }


    @Bean
    public Binding bindingorange() {
        Binding binding = BindingBuilder.bind(directQueue1()).to(directExchange()).with("order");
        return binding;
    }
}
