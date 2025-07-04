package com.example.gautoi.integration.kafka.listener;

import com.example.gautoi.annotation.AuditableLog;
import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.entity.PersonEvent;
import com.example.gautoi.integration.kafka.service.PersonConsumerService;
import com.example.gautoi.util.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonListener {
    private final PersonConsumerService personServiceKafkaHandler;

    @KafkaListener(topics = KafkaConstants.PERSON_TOPIC, groupId = KafkaConstants.GROUP_ID, containerFactory = KafkaConstants.GROUP_KAFKA_FACTORY, batch = "true")
    @AuditableLog(SourceType.CONSUMER)
    public void consumePersonService(ConsumerRecords<String, PersonEvent> personEventRecords, Acknowledgment ack) {
        log.info("consumePersonService: {}", personEventRecords);
        for (ConsumerRecord<String, PersonEvent> record : personEventRecords) {
            PersonEvent person = record.value();
            switch (person.getEventType()) {
                case CREATE -> personServiceKafkaHandler.handleCreatePersonEvent(record);
                case UPDATE -> personServiceKafkaHandler.handleUpdatePersonEvent(person);
                case DELETE -> personServiceKafkaHandler.handleDeletePersonEvent(person);
                default -> throw new IllegalArgumentException("Unexpected value: " + person.getEventType());
            }
        }
        ack.acknowledge();
    }

}
