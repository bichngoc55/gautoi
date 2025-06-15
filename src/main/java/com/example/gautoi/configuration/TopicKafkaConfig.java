package com.example.gautoi.configuration;


import com.example.gautoi.constant.KafkaConstants;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class TopicKafkaConfig {

    @Bean
    public NewTopic personTopic() {
        return new NewTopic(KafkaConstants.PERSON_TOPIC, 1, (short) 1);
    }

}
