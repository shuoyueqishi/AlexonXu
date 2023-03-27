package com.xlt;


import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.xlt.mapper")
@NacosPropertySource(dataId = "chat-gpt.yml", autoRefreshed = true)
public class ChatGPTApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatGPTApplication.class, args);
    }
}
