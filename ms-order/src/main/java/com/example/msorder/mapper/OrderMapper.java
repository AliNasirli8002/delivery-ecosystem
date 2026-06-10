package com.example.msorder.mapper;

import com.example.msorder.dao.Order;
import com.example.msorder.dto.OrderCreateRequest;
import com.example.msorder.dto.OrderResponse;
import com.example.msorder.enums.OrderStatus;

import java.math.BigDecimal;

public final class OrderMapper {

    private OrderMapper() {
        throw new UnsupportedOperationException("Utility mapping class cannot be instantiated");
    }

    public static Order toEntity(OrderCreateRequest request, Long courierId, BigDecimal price, OrderStatus status) {
        if (request == null) {
            return null;
        }

        return Order.builder()
                .description(request.getDescription())
                .courierId(courierId)
                .price(price)
                .status(status)
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponse.builder()
                .id(order.getId())
                .description(order.getDescription())
                .price(order.getPrice())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .courierId(order.getCourierId())
                .build();
    }
}