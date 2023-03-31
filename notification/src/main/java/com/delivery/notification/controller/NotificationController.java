package com.delivery.notification.controller;

import com.delivery.clients.notification.NotificationRequest;
import com.delivery.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("newNotification")
    public void sendNotification(@RequestBody NotificationRequest notificationRequest){
        notificationService.sendNotification(notificationRequest);
    }
}
