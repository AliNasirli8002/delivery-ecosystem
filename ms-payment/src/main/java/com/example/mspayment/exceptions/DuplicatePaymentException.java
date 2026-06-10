package com.example.mspayment.exceptions;

public class DuplicatePaymentException extends RuntimeException {
    public DuplicatePaymentException(Long orderId) {
        super("Transaction rejected. Payment tracking record already initialized for order ID: " + orderId);
    }
}