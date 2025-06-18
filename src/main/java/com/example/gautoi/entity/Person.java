package com.example.gautoi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.processing.Pattern;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person")
@Entity
@Builder
public class Person {
    @Id
    @Column(name = "tax_number", length = 8, nullable = false, unique = true)
    private String taxNumber;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name="tax_debt" )
    private Double taxDebt;
}
