package com.example.gautoi.annotation;

import com.example.gautoi.entity.AuditLogging;
import com.example.gautoi.entity.GauEvent;
import com.example.gautoi.repository.AuditLoggingRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuditHelper {
    private final AuditLoggingRepository loggingRepository;

    //    controller, producer, database ,  listener ,
//        database (service ) - destination: database,  ten ham dang duoc log , payload la arg cua ham,
//        producer : destination : topic , ham : producer , arg ...???
//        consumer : destination :  topic name, ham: listener , payload la arg ,

    public void handleControllerAudit(ProceedingJoinPoint joinPoint, Throwable error) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof List<?>) {
                for (Object b : (List<?>) arg) {
                    if (b instanceof GauEvent gauEvent) {
                        String result = "{" + gauEvent.getGauName() + ", " + gauEvent.getGauAge() + "}";
                        builder.append(result);
                    }
                }
            }
//
        }
        String errorMessage = (error != null) ? error.getMessage() : null;
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        loggingRepository.save(new AuditLogging(null, builder.toString(), LocalDateTime.now(), request.getRequestURI(), joinPoint.getSignature().toShortString(), errorMessage));
    }

    public void handleServiceAudit(ProceedingJoinPoint joinPoint, Throwable error) {
        String errorMessage = (error != null) ? error.getMessage() : null;
        loggingRepository.save(new AuditLogging(null,
                Arrays.toString(joinPoint.getArgs()),
                LocalDateTime.now(), "database",
                joinPoint.getSignature().toShortString(),
                errorMessage));
    }

    public void handleConsumerAudit(ProceedingJoinPoint joinPoint, Throwable error) {
        Object[] args = joinPoint.getArgs();
        String destination = null;
        for (Object arg : args) {
            if (arg instanceof ConsumerRecords) {
                if (((ConsumerRecords<?, ?>) arg).count() > 0) {
                    ConsumerRecords<?, ?> records = (ConsumerRecords<?, ?>) arg;
                    ConsumerRecord<?, ?> record = records.iterator().next();
                    destination = record.topic();
                    break;
                }
            }
        }
        String errorMessage = (error != null) ? error.getMessage() : null;
        StringBuilder payloadBuilder = new StringBuilder("[");
        if (args.length > 0 && args[0] instanceof ConsumerRecords<?, ?> consumerRecords) {
            for (ConsumerRecord<?, ?> recordObj : consumerRecords) {
                payloadBuilder.append("{key: ").append(recordObj.key()).append(", value: ").append(recordObj.value()).append("}, ");
            }
        }
        payloadBuilder.append("]");
        loggingRepository.save(new AuditLogging(null, payloadBuilder.toString(), LocalDateTime.now(), destination, joinPoint.getSignature().toShortString(), errorMessage));
    }

}
