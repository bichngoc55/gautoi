package com.example.gautoi.configuration;

import com.example.gautoi.entity.PersonEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@Configuration
@Slf4j
public class ConsumerKafkaConfig {

    @Bean
    KafkaProperties kafkaProperties() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        log.info("kafkaProperties: {}", kafkaProperties);
        return kafkaProperties;
    }

    @Bean
    public ConsumerFactory<String, PersonEvent> consumerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties() );
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PersonEvent> factory =  new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(kafkaProperties()));
        return factory;
    }

}