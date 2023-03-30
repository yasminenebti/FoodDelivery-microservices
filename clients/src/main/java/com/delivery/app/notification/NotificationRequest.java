package com.delivery.app.notification;

public record NotificationRequest(
        Integer orderId,
        String message
) {
}
