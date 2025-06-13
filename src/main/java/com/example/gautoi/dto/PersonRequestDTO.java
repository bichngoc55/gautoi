package com.example.gautoi.dto;

import java.time.LocalDate;

public record PersonRequestDTO(
        String taxNumber,
        String lastName,
        String firstName,
        LocalDate dateOfBirth
) {
}
