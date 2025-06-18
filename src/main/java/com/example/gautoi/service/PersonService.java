package com.example.gautoi.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;

import java.time.LocalDate;
import java.util.List;

 public interface PersonService {
    List<PersonResponseDTO> getPeople();
    PersonResponseDTO findPersonByTaxNumber(String taxNumber);
    PersonResponseDTO createPerson(PersonRequestDTO person);
    PersonResponseDTO updatePerson(PersonRequestDTO person);
    void deletePerson(String taxNumber);
    List<PersonResponseDTO> findPeopleByNameAndAge(String name, LocalDate date);
}
