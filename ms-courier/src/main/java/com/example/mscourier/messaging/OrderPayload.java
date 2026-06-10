package com.example.mscourier.messaging;

import lombok.Data;

@Data
public class OrderPayload {
    private Long orderId;
    private Long courierId;
}