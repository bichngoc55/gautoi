package com.example.gautoi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
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
    @org.hibernate.annotations.Index(name = "lastname_index")
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    @Column(name="tax_debt" )
    private Double taxDebt;
}
