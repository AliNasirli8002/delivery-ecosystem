package com.example.mscourier.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ASSIGNED_QUEUE = "courier.order-assigned.queue";
    public static final String DELIVERED_QUEUE = "courier.order-delivered.queue";

    public static final String ASSIGNED_ROUTING_KEY = "order.assigned";
    public static final String DELIVERED_ROUTING_KEY = "order.delivered";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue assignedQueue() {
        return new Queue(ASSIGNED_QUEUE, true);
    }

    @Bean
    public Queue deliveredQueue() {
        return new Queue(DELIVERED_QUEUE, true);
    }

    @Bean
    public Binding bindingAssigned(Queue assignedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(assignedQueue).to(orderExchange).with(ASSIGNED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingDelivered(Queue deliveredQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(deliveredQueue).to(orderExchange).with(DELIVERED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}