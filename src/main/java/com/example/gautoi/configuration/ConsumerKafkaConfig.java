package com.example.gautoi.configuration;

import com.example.gautoi.entity.PersonEvent;
import com.example.gautoi.entity.TaxCalculationEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

@Configuration
@Slf4j
public class ConsumerKafkaConfig {

    @Bean
    KafkaProperties kafkaProperties() {
        KafkaProperties kafkaProperties = new KafkaProperties();
//        null het
        log.info("kafkaProperties: {}", kafkaProperties);
        log.info("bootstrap.servers: {}", kafkaProperties.getBootstrapServers());
        log.info("group.id: {}", kafkaProperties.getConsumer().getGroupId());
        log.info("auto.offset.reset: {}", kafkaProperties.getConsumer().getAutoOffsetReset());
        log.info("enable.auto.commit: {}", kafkaProperties.getConsumer().getEnableAutoCommit());
        return kafkaProperties;
    }
// Person consumer factory
    @Bean
    public ConsumerFactory<String, PersonEvent> personConsumerFactory(KafkaProperties kafkaProperties) {
        ErrorHandlingDeserializer<PersonEvent> valueDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(PersonEvent.class, false));
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());

        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonEvent> personKafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, PersonEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(personConsumerFactory(kafkaProperties));
        return factory;
    }
// van hoi confused why tao tu dong z ? kafka listener tu dong tao group id vs topic a
    // tax consumer factory
    @Bean
    public ConsumerFactory<String, TaxCalculationEvent> taxConsumerFactory(KafkaProperties kafkaProperties) {
        ErrorHandlingDeserializer<TaxCalculationEvent> valueDeserializer = new ErrorHandlingDeserializer<>(new JsonDeserializer<>(TaxCalculationEvent.class, false));
        ErrorHandlingDeserializer<String> keyDeserializer = new ErrorHandlingDeserializer<>(new StringDeserializer());
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaxCalculationEvent> taxKafkaListenerContainerFactory(KafkaProperties kafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, TaxCalculationEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(taxConsumerFactory(kafkaProperties));
        return factory;
    }

}