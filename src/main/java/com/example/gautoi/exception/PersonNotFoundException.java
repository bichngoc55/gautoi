package com.example.gautoi.exception;

public class PersonNotFoundException extends NonRetryException {
    public PersonNotFoundException(String message) {
        super(message);
    }

}
