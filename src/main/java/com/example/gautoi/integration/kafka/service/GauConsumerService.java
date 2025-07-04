package com.example.gautoi.integration.kafka.service;

import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.dto.GauResponseDTO;
import com.example.gautoi.entity.GauEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GauConsumerService {
    private final ConsumerFactory<String, GauEvent> gauEventConsumerFactory;

    public GauResponseDTO consumeGauEvent() {
        Consumer<String, GauEvent> gauConsumer = gauEventConsumerFactory.createConsumer(KafkaConstants.GAU_GROUP, KafkaConstants.GAU_TOPIC);
        try (gauConsumer) {
            gauConsumer.subscribe(Collections.singletonList(KafkaConstants.GAU_TOPIC));
            ConsumerRecords<String, GauEvent> records = gauConsumer.poll(Duration.ofMillis(3000));
            List<GauEvent> events = new ArrayList<>();
            for (ConsumerRecord<String, GauEvent> record : records) {
                log.info("record: {}", record);
                GauEvent gauEvent = record.value();
                if (record.value() == null) {
                    continue;
                }
                events.add(gauEvent);
            }
            Set<TopicPartition> assignedPartitions = gauConsumer.assignment();
            long totalLag = 0;

            if (!assignedPartitions.isEmpty()) {
                Map<TopicPartition, Long> endOffsets = gauConsumer.endOffsets(assignedPartitions);
                for (TopicPartition tp : assignedPartitions) {
                    long endOffset = endOffsets.get(tp);
                    long currentOffset = gauConsumer.position(tp);
                    long lag = endOffset - currentOffset;
                    totalLag += lag;
                }
            } else {
                gauConsumer.commitSync();
                return new GauResponseDTO(events,totalLag,true);
            }

            gauConsumer.commitSync();
            boolean hasMoreMessages = totalLag > 0;
            return new GauResponseDTO(events, totalLag, hasMoreMessages);

        }
    }
}
