package com.delivery.app.service;

import com.delivery.app.entity.Notification;
import com.delivery.app.notification.NotificationRequest;
import com.delivery.app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void sendNotification(NotificationRequest notificationRequest){
        notificationRepository.save(
                Notification.builder()
                        .orderId(notificationRequest.orderId())
                        .sender("sender")
                        .message("new Order placed successfully")
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }

}
