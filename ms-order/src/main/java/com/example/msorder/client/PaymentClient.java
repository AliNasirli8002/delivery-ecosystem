package com.example.msorder.client;

import com.example.msorder.client.dto.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-payment", url = "http://localhost:8083/api/v1/payments")
public interface PaymentClient {

    @GetMapping("/order/{orderId}")
    PaymentResponseDto getPaymentByOrderId(@PathVariable("orderId") Long orderId);
}