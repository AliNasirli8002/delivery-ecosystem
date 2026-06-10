package com.example.msorder.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private String status;
    private BigDecimal deliveryFee;
    private BigDecimal courierEarning;
    private LocalDateTime createdAt;
}