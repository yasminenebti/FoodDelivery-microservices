package com.delivery.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "com.delivery.order",
        "com.delivery.app.amqp"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.delivery.clients")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
        System.out.println("Hello from order service!");
    }
}