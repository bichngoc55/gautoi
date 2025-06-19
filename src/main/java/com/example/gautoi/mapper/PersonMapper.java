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
                .taxDebt(0.0)
                .build();
    }
    public static PersonResponseDTO toResponseDTO(Person person) {
        int age = Period.between(person.getDateOfBirth(), LocalDate.now()).getYears();
//        them tax debt o day :(
        Double debt = person.getTaxDebt() == null ? 0 : person.getTaxDebt();
        return new PersonResponseDTO(person.getTaxNumber(), person.getFirstName(), person.getLastName(), age, debt);
    }
}
