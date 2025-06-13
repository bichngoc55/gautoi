package com.example.gautoi.dto;


public record PersonResponseDTO(
        String taxNumber,
        String lastName,
        String firstName,
        int age
) {
}
