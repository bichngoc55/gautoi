package com.example.gautoi.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import java.util.List;
import java.util.Optional;
public interface PersonService {
    List<PersonResponseDTO> getPeople();
    PersonResponseDTO findPersonByTaxNumber(String taxNumber);
    PersonResponseDTO createPerson(PersonRequestDTO person);
    PersonResponseDTO updatePerson(PersonRequestDTO person);
    void deletePerson(String taxNumber);
    List<PersonResponseDTO> findPeopleByNameAndAge(String name);

}
