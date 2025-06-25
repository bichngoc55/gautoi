package com.example.gautoi.exception;

public class TaxValidationException extends NonRetryException {
    public TaxValidationException(String message) {
        super(message);
    }
}
