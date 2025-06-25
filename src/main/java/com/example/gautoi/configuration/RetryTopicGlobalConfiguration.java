package com.example.gautoi.configuration;

import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.exception.SimulatePersonRunTimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class RetryTopicGlobalConfiguration extends RetryTopicConfigurationSupport {
    private final KafkaRetryProperties retryProperties;
// dung retryable topic
    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, Object> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(retryProperties.getBackoff() )
                .maxAttempts(retryProperties.getMaxAttempts() )
                .concurrency(retryProperties.getConcurrency() )
                .includeTopic(KafkaConstants.PERSON_TOPIC)
                .retryOn(SimulatePersonRunTimeException.class)
                .create(template);
    }
}
