package com.example.gautoi.entity;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.util.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonEvent {
    private EventType eventType;
    private PersonRequestDTO person;
}
