package com.example.mspayment.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_CREATED_PAYMENT_QUEUE = "order.created.payment.queue";

    @Bean
    public Queue paymentQueue() {
        return new Queue(ORDER_CREATED_PAYMENT_QUEUE, true);
    }
}