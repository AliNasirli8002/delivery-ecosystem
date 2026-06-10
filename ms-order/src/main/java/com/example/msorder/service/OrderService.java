package com.example.msorder.service;

import com.example.msorder.client.CourierClient;
import com.example.msorder.client.PaymentClient;
import com.example.msorder.client.dto.CourierResponseDto;
import com.example.msorder.client.dto.PaymentResponseDto;
import com.example.msorder.criteria.OrderCriteria;
import com.example.msorder.criteria.PageCriteria;
import com.example.msorder.dao.Order;
import com.example.msorder.dao.OrderRepository;
import com.example.msorder.dto.OrderCreateRequest;
import com.example.msorder.dto.OrderResponse;
import com.example.msorder.enums.OrderStatus;
import com.example.msorder.events.OrderAssignedEvent;
import com.example.msorder.events.OrderCreatedEvent;
import com.example.msorder.exceptions.CourierUnavailableException;
import com.example.msorder.exceptions.InvalidOrderStatusException;
import com.example.msorder.exceptions.OrderNotFoundException;
import com.example.msorder.mapper.OrderMapper;
import com.example.msorder.specification.OrderSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CourierClient courierClient;
    private final PaymentClient paymentClient; // 🚀 Added to query payment information
    private final AmqpTemplate amqpTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final ObjectMapper objectMapper;

    public static final String ORDER_CREATED_PAYMENT_QUEUE = "order.created.payment.queue";
    public static final String ORDER_ASSIGNED_COURIER_QUEUE = "order.assigned.courier.queue";

    @Transactional
    public void createOrder(OrderCreateRequest request) {
        log.info("Processing order creation request: {}", request);

        BigDecimal calculatedPrice = calculatePrice(request);
        CourierResponseDto assignedCourier = fetchAvailableCourier();

        Order order = OrderMapper.toEntity(request, assignedCourier.getId(), calculatedPrice, OrderStatus.ASSIGNED);
        Order savedOrder = orderRepository.save(order);
        log.info("Order successfully persisted with ID: {}", savedOrder.getId());

        dispatchAsyncEvents(savedOrder, assignedCourier);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrders(OrderCriteria orderCriteria, PageCriteria pageCriteria) {
        int pageNumber = (pageCriteria != null && pageCriteria.getPageNumber() != null) ? pageCriteria.getPageNumber() : 0;
        int count = (pageCriteria != null && pageCriteria.getCount() != null) ? pageCriteria.getCount() : 10;
        PageRequest pageable = PageRequest.of(pageNumber, count);

        Page<Order> orderPage = orderRepository.findAll(
                new OrderSpecification(orderCriteria),
                pageable
        );

        return orderPage.map(OrderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = fetchOrderIfExists(id);
        OrderResponse orderResponse = OrderMapper.toResponse(order);

        try {
            CourierResponseDto courier = courierClient.getCourierById(order.getCourierId());
            if (courier != null) {
                orderResponse.setCourierName(courier.getName());
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve companion courier details for driver ID: {}. Processing fallback layout context.",
                    order.getCourierId(), e);
        }

        try {
            PaymentResponseDto payment = paymentClient.getPaymentByOrderId(id);
            if (payment != null) {
                orderResponse.setPaymentStatus(payment.getStatus());
                orderResponse.setPaymentProcessedAt(payment.getCreatedAt());
            }
        } catch (Exception e) {
            log.warn("Could not enrich payment tracking matrix metrics for order index key reference: {}. Defaulting values.", id, e);
            orderResponse.setPaymentStatus("UNPAID");
            orderResponse.setPaymentProcessedAt(null);
        }

        return orderResponse;
    }

    @Transactional
    public void updateStatusToDelivered(Long id) {
        Order order = fetchOrderIfExists(id);

        if (order.getStatus() != OrderStatus.ASSIGNED) {
            throw new InvalidOrderStatusException("Order can be delivered only when status is ASSIGNED. Current status: "
                    + order.getStatus());
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        log.info("Order ID: {} successfully advanced to DELIVERED status state.", id);
    }

    private CourierResponseDto fetchAvailableCourier() {
        PageCriteria defaultCriteria = new PageCriteria(0, 10);
        Page<CourierResponseDto> courierPage = null;

        try {
            courierPage = courierClient.getAvailableCouriers(defaultCriteria);
        } catch (Exception e) {
            log.error("Sync connection break during fallback OpenFeign communications framework execution", e);
            throw new CourierUnavailableException("Courier lookup failed due to network integration timeout channels.");
        }

        if (courierPage == null || courierPage.getContent().isEmpty()) {
            throw new CourierUnavailableException("No active drivers are available to take this assignment right now.");
        }

        return courierPage.getContent().get(0);
    }

    private Order fetchOrderIfExists(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    private BigDecimal calculatePrice(OrderCreateRequest request) {
        return BigDecimal.valueOf(10.00);
    }

    private void dispatchAsyncEvents(Order order, CourierResponseDto courier) {
        try {
            String createdEventJson = objectMapper.writeValueAsString(
                    new OrderCreatedEvent(order.getId(), courier.getId(), order.getPrice())
            );
            amqpTemplate.convertAndSend(ORDER_CREATED_PAYMENT_QUEUE, createdEventJson);

            String assignedEventJson = objectMapper.writeValueAsString(
                    new OrderAssignedEvent(courier.getId())
            );
            amqpTemplate.convertAndSend(ORDER_ASSIGNED_COURIER_QUEUE, assignedEventJson);

            log.info("Asynchronous AMQP transaction logs successfully written into message broker networks.");
        } catch (JsonProcessingException e) {
            log.error("Fatal exception during Jackson serialization handling routines.", e);
            throw new RuntimeException("Failed to serialize messaging event components to queue payloads.", e);
        }
    }
}