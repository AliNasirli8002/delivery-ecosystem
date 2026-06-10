package com.example.mscourier.exception;

public class NoAvailableCourierException extends RuntimeException {
    public NoAvailableCourierException(String message) {
        super(message);
    }
}