package com.example.mscourier.messaging;

import com.example.mscourier.exception.CourierNotFoundException;
import com.example.mscourier.enums.CourierStatus;
import com.example.mscourier.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CourierService courierService;

    @RabbitListener(queues = "courier.order-assigned.queue")
    public void handleOrderAssigned(OrderPayload payload) {
        log.info("Processing ORDER_ASSIGNED event for Courier ID: {}", payload.getCourierId());
        try {
            courierService.updateCourierStatus(payload.getCourierId(), CourierStatus.BUSY);
        } catch (CourierNotFoundException e) {
            log.error("Event processing dropped: Target courier does not exist in our system database.");
        }
    }

    @RabbitListener(queues = "courier.order-delivered.queue")
    public void handleOrderDelivered(OrderPayload payload) {
        log.info("Received ORDER_DELIVERED event for Courier ID: {}", payload.getCourierId());
        try {
            courierService.updateCourierStatus(payload.getCourierId(), CourierStatus.FREE);
        } catch (Exception e) {
            log.error("Failed to update courier status to FREE: {}", e.getMessage());
        }
    }
}