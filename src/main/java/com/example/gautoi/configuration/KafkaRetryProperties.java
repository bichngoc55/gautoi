package com.example.gautoi.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.retry")
public class KafkaRetryProperties {
    private long backoff;
    private  int maxAttempts;
    private int concurrency;
    private List<String> includeTopics;
}
