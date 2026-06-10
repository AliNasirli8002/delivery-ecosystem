package com.example.mspayment.queue.consumer;

import com.example.mspayment.config.RabbitMQConfig;
import com.example.mspayment.queue.event.OrderCreatedEvent;
import com.example.mspayment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventConsumer {

    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_PAYMENT_QUEUE)
    public void consumeOrderCreatedMessage(String rawJsonMessage) {
        log.info("Received transaction payload from RabbitMQ wire: {}", rawJsonMessage);
        try {
            OrderCreatedEvent event = objectMapper.readValue(rawJsonMessage, OrderCreatedEvent.class);
            paymentService.processOrderPayment(event);
        } catch (Exception ex) {
            log.error("Failed to parse or process payload data format structural criteria matches", ex);
        }
    }
}