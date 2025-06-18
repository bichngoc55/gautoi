package com.example.gautoi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TaxCalculationEvent {
    Double amount;
    String taxNumber;
}
