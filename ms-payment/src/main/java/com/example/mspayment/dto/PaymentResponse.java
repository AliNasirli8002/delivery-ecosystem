package com.example.mspayment.dto;

import com.example.mspayment.enums.PaymentStatus;
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
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long courierId;
    private Long courierBalanceId;
    private BigDecimal deliveryFee;
    private BigDecimal courierEarning;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}