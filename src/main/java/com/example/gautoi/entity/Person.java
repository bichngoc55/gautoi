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
@Table(name="Person")
@Entity
@Builder
public class Person {
    @Id
//    Tax number là 1 chuỗi 8 number , Không có field nào được null cả
    @Column(name = "tax_number", nullable = false, unique = true)
    private String taxNumber;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
//    dateOfBirth Không lớn hơn ngày hiện tại
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
}
