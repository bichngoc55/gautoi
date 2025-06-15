package com.example.gautoi.integration.kafka.event;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.util.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEvent {
    private EventType eventType;
    private PersonRequestDTO person;
}
