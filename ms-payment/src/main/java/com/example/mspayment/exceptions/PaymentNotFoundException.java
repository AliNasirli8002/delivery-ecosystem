package com.example.mspayment.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long id) {
        super("Payment record could not be found matching reference token: " + id);
    }
}