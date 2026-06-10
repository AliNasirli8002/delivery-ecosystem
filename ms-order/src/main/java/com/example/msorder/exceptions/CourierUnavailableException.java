package com.example.msorder.exceptions;

public class CourierUnavailableException extends RuntimeException {
    public CourierUnavailableException(String message) {
        super(message);
    }
}