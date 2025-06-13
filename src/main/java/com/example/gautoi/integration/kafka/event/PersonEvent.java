package com.example.gautoi.integration.kafka.event;

import com.example.gautoi.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEvent {
    private String eventType; // CrEATE, UPDATE, DELETE
    private Person person;
    private Long personId;
}
