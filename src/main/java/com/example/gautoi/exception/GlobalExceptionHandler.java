package com.example.gautoi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePersonAlreadyExistsException(PersonAlreadyExistsException e) {
        log.error("PersonAlreadyExistsException: {}", e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePersonNotFoundException(PersonNotFoundException e) {
        log.error("PersonNotFoundException: {}", e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonValidationException.class)
    public ResponseEntity<Map<String, Object>> handlePersonValidationException(PersonValidationException e) {
        log.error("PersonValidationException: {}", e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaxValidationException.class)
    public ResponseEntity<Map<String, Object>> handleTaxValidationException(TaxValidationException e) {
        log.error("TaxValidationException: {}", e.getMessage());
        return buildResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);
        return buildResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus status) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", status.value());
//        response.put("error", status.getReasonPhrase());
//        response.put("message", message);
//        return new ResponseEntity<>(response, status);
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        return new ResponseEntity<>(errorDetails, status);
    }
}
