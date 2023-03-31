package com.delivery.notification.consumer;

import com.delivery.clients.notification.NotificationRequest;
import com.delivery.notification.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationConsumerFromAmqp {

    private final NotificationService notificationService;
    @RabbitListener(queues = "${rabbitmq.queue.notification}")
    public void consumeNotification(NotificationRequest notificationRequest){
        log.info("the consumed notification {} from queue is sent to the database successfully" , notificationRequest);
        notificationService.sendNotification(notificationRequest);
    }

}
