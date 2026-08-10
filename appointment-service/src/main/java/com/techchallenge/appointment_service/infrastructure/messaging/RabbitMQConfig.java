package com.techchallenge.appointment_service.infrastructure.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NOTIFICATION = "notification.queue";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATION, true);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}