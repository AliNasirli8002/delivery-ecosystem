package com.example.mspayment.mapper;

import com.example.mspayment.dao.entity.CourierBalance;
import com.example.mspayment.dao.entity.Payment;
import com.example.mspayment.dto.PaymentResponse;
import com.example.mspayment.enums.PaymentStatus;
import com.example.mspayment.queue.event.OrderCreatedEvent;

import java.math.BigDecimal;

public final class PaymentMapper {

    private PaymentMapper() {
        throw new UnsupportedOperationException("Utility mapping engine cannot be initialized");
    }

    public static Payment toEntity(OrderCreatedEvent event, CourierBalance balance, PaymentStatus status) {
        if (event == null) {
            return null;
        }

        BigDecimal calculatedEarning = event.getPrice() != null
                ? event.getPrice().multiply(BigDecimal.valueOf(0.80))
                : BigDecimal.ZERO;

        return Payment.builder()
                .orderId(event.getOrderId())
                .courierId(event.getCourierId())
                .deliveryFee(event.getPrice())
                .courierEarning(calculatedEarning)
                .status(status)
                .courierBalance(balance)
                .build();
    }

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .courierId(payment.getCourierId())
                .courierBalanceId(payment.getCourierBalance() != null ? payment.getCourierBalance().getId() : null)
                .deliveryFee(payment.getDeliveryFee())
                .courierEarning(payment.getCourierEarning())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}