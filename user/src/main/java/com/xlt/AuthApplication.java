package com.xlt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

//@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = "com.xlt.mapper")
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
