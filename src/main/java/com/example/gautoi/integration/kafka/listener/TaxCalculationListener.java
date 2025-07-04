package com.example.gautoi.integration.kafka.listener;

import com.example.gautoi.annotation.AuditableLog;
import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.entity.TaxCalculationEvent;
import com.example.gautoi.exception.MaximumAmountExceedException;
import com.example.gautoi.integration.kafka.service.TaxCalculationConsumerService;
import com.example.gautoi.util.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationListener {

    private final TaxCalculationConsumerService taxCalculationServiceKafka;
    @AuditableLog(SourceType.CONSUMER)
    @KafkaListener(containerFactory = KafkaConstants.TAX_KAFKA_FACTORY, groupId = KafkaConstants.TAX_GROUP, topics = KafkaConstants.TAX_CALCULATION_TOPIC, batch = "true")
    public void listen(ConsumerRecords<String, TaxCalculationEvent> taxRecords, Acknowledgment ack) {
        taxCalculationServiceKafka.consumeTaxBatchCalculation(taxRecords);
        ack.acknowledge();

    }
}
