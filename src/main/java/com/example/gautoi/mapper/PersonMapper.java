package com.example.gautoi.mapper;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import java.time.LocalDate;
import java.time.Period;

public class PersonMapper {
    public static Person toEntity(PersonRequestDTO personRequestDTO) {
        return Person.builder()
                .taxNumber(personRequestDTO.taxNumber())
                .firstName(personRequestDTO.firstName())
                .lastName(personRequestDTO.lastName())
                .dateOfBirth(personRequestDTO.dateOfBirth())
                .build();
    }
    public static PersonResponseDTO toResponseDTO(Person person) {
        int age = Period.between(person.getDateOfBirth(), LocalDate.now()).getYears();
        return new PersonResponseDTO(person.getTaxNumber(), person.getFirstName(), person.getLastName(), age);
    }
}
