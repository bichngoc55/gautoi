package com.example.gautoi.exception;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.BatchListenerFailedException;

public class MaximumAmountExceedException extends BatchListenerFailedException {
    public MaximumAmountExceedException(String message, ConsumerRecord record) {
        super(message, record);

    }
}
