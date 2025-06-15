package com.example.gautoi.configuration;

import com.example.gautoi.integration.kafka.event.PersonEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class ConsumerKafkaConfig {
    private final KafkaProperties kafkaProperties;
//    sao 2 cai lan z
    @Bean
    public ConsumerFactory<String, PersonEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
    }
    @Bean
    public KafkaProperties kafkaProperties() {
        log.info("Initializing Kafka Properties");
        KafkaProperties kafkaProperties = new KafkaProperties();
        log.debug("Initialized Kafka Properties: {}", kafkaProperties);
        return kafkaProperties;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PersonEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}