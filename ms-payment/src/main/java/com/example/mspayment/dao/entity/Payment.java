package com.example.mspayment.dao.entity;

import com.example.mspayment.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull(message = "Order ID cannot be null")
    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @NotNull(message = "Courier ID cannot be null")
    @Column(name = "courier_id", nullable = false)
    private Long courierId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "courier_balance_id")
    private CourierBalance courierBalance;

    @NotNull(message = "Delivery fee cannot be null")
    @DecimalMin(value = "0.00", message = "Delivery fee cannot be negative")
    @Column(name = "delivery_fee", nullable = false, precision = 19, scale = 2)
    private BigDecimal deliveryFee;

    @DecimalMin(value = "0.00", message = "Courier earning cannot be negative")
    @Column(name = "courier_earning", precision = 19, scale = 2)
    private BigDecimal courierEarning;

    @NotNull(message = "Payment status cannot be null")
    @Enumerated(STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}