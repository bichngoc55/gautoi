package com.example.gautoi.exception;

public class PersonValidationException extends NonRetryException  {
    public PersonValidationException(String message) {
        super("Person validation failed: " + message);
    }

}
