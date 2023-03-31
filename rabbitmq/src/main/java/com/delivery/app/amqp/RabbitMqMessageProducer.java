package com.delivery.app.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitMqMessageProducer {
    private final AmqpTemplate amqpTemplate;
    public void publish(Object payload, String exchange , String routingKey) {
        log.info("Publishing to {} using routingKey {}. Payload: {}" , exchange , routingKey , payload);
        amqpTemplate.convertAndSend(exchange , routingKey , payload);
        log.info("Published to {} using routingKey {}. Payload: {}" , exchange , routingKey , payload);

    }
}
