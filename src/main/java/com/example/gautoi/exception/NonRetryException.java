package com.example.gautoi.exception;

public class NonRetryException extends RuntimeException {
    public NonRetryException(String message) {
        super(message);
    }

}
