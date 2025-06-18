package com.example.gautoi.exception;

public class PersonValidationException extends RuntimeException {
    public PersonValidationException(String message) {
        super("Person validation failed: " + message);
    }

}
