package com.example.gautoi.exception;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchListenerFailedException;

public class SimulatePersonRunTimeException extends BatchListenerFailedException {
    public SimulatePersonRunTimeException(String message, ConsumerRecord record) {
        super(message, record);
    }
}
