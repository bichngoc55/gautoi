package com.example.gautoi.dto;

import com.example.gautoi.entity.GauEvent;

import java.util.List;

public record GauResponseDTO(List<GauEvent> batchOfEvents,
                             long messageLeftInTopic,
                             boolean hasMoreMessages) {
}
