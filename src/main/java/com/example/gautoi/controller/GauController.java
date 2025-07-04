package com.example.gautoi.controller;

import com.example.gautoi.annotation.AuditableLog;
import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.dto.GauResponseDTO;
import com.example.gautoi.entity.GauEvent;
import com.example.gautoi.integration.kafka.service.GauConsumerService;
import com.example.gautoi.util.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/gau")
@RequiredArgsConstructor
public class GauController {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final GauConsumerService gauConsumerService;
// request --> controller
    @AuditableLog(SourceType.CONTROLLER)
    @PostMapping
    public ResponseEntity<Object> senGauEvent(@RequestBody List<GauEvent> gauEvent) {
        for (GauEvent e : gauEvent) {
            kafkaTemplate.send(KafkaConstants.GAU_TOPIC, e);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Send gau events successfully");
        response.put("count", gauEvent.size());
        response.put("status", HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
    @AuditableLog(SourceType.CONTROLLER)
    @GetMapping
    public ResponseEntity<GauResponseDTO> getGauData( ) {
        return ResponseEntity.status(HttpStatus.OK).body(gauConsumerService.consumeGauEvent());
    }
}
