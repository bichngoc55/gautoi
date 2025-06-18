package com.example.gautoi.exception;

public class PersonAlreadyExistsException extends RuntimeException{
    public PersonAlreadyExistsException( String taxNumber) {

        super("Person with this tax number already exist "+  taxNumber );
    }
}
