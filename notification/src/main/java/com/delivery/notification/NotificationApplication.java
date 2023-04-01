package com.delivery.notification;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(
        scanBasePackages = {
                "com.delivery.notification",
                "com.delivery.app.amqp"
        }
)
@EnableDiscoveryClient
public class NotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
        System.out.println("Hello from notification service!");
    }

}