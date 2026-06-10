package com.example.msorder.controller;

import com.example.msorder.criteria.OrderCriteria;
import com.example.msorder.criteria.PageCriteria;
import com.example.msorder.dto.OrderCreateRequest;
import com.example.msorder.dto.OrderResponse;
import com.example.msorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@RequestBody OrderCreateRequest request) {
        orderService.createOrder(request);
    }

    @GetMapping
    public Page<OrderResponse> getOrders(OrderCriteria orderCriteria, PageCriteria pageCriteria) {
        return orderService.getOrders(orderCriteria, pageCriteria);
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/deliver")
    public void updateStatusToDelivered(@PathVariable Long id) {
        orderService.updateStatusToDelivered(id);
    }
}