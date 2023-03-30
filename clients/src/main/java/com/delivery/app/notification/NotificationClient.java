package com.delivery.app.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "notification" , url = "http://localhost:8083")
public interface NotificationClient {
     @PostMapping("notification/newNotification")
     NotificationRequest sendNotification(@RequestBody NotificationRequest notificationRequest);
}
