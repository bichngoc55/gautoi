package com.example.gautoi.integration.kafka.listener;

import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.entity.TaxCalculationEvent;
import com.example.gautoi.exception.MaximumAmountExceedException;
import com.example.gautoi.integration.kafka.service.TaxCalculationServiceKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationListener {

    private final TaxCalculationServiceKafka taxCalculationServiceKafka;
    @KafkaListener(containerFactory = KafkaConstants.TAX_KAFKA_FACTORY, groupId = KafkaConstants.TAX_GROUP, topics = KafkaConstants.TAX_CALCULATION_TOPIC)
    public void listen(List<ConsumerRecord<String, TaxCalculationEvent>> taxRecords, Acknowledgment ack) {

        for (ConsumerRecord<String, TaxCalculationEvent> taxRecord : taxRecords) {
            try{
                taxCalculationServiceKafka.consumeTaxCalculation(taxRecord);
            }catch(MaximumAmountExceedException e){
                throw new MaximumAmountExceedException(e.getMessage(), taxRecord);
            }
        }
        ack.acknowledge();
    }
}
