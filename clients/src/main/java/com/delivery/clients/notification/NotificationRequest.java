package com.delivery.clients.notification;

public record NotificationRequest(
        Integer orderId,
        String message
) {
}
