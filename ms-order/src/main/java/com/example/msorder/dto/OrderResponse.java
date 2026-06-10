package com.example.msorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String description;
    private Long courierId;
    private String courierName;
    private BigDecimal price;
    private String status;
    private String paymentStatus;
    private LocalDateTime paymentProcessedAt;
}