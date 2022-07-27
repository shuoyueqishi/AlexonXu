package com.xlt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xlt.mapper")
public class CommodityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommodityApplication.class, args);
    }
}
