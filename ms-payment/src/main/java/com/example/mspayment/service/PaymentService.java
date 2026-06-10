package com.example.mspayment.service;

import com.example.mspayment.dao.entity.CourierBalance;
import com.example.mspayment.dao.entity.Payment;
import com.example.mspayment.dao.repository.CourierBalanceRepository;
import com.example.mspayment.dao.repository.PaymentRepository;
import com.example.mspayment.dto.PaymentResponse;
import com.example.mspayment.enums.PaymentStatus;
import com.example.mspayment.exceptions.DuplicatePaymentException;
import com.example.mspayment.exceptions.PaymentNotFoundException;
import com.example.mspayment.mapper.PaymentMapper;
import com.example.mspayment.queue.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CourierBalanceRepository courierBalanceRepository;


    @Transactional
    public void processOrderPayment(OrderCreatedEvent event) {
        log.info("Processing billing events for Order ID: {}", event.getOrderId());

        if (paymentRepository.findByOrderId(event.getOrderId()).isPresent()) {
            throw new DuplicatePaymentException(event.getOrderId());
        }

        CourierBalance balance = courierBalanceRepository.findByCourierId(event.getCourierId())
                .orElseGet(() -> CourierBalance.builder()
                        .courierId(event.getCourierId())
                        .balance(BigDecimal.ZERO)
                        .build());

        Payment payment = PaymentMapper.toEntity(event, balance, PaymentStatus.SUCCESS);
        BigDecimal updatedBalanceAmount = balance.getBalance().add(payment.getCourierEarning());
        balance.setBalance(updatedBalanceAmount);
        paymentRepository.save(payment);
        log.info("Transaction saved. Payment ID: {}, New Courier Wallet Balance: ${}",
                payment.getId(), updatedBalanceAmount);
    }


    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .map(PaymentMapper::toResponse)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));
    }
}