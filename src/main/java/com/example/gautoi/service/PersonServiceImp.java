package com.example.gautoi.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImp implements PersonService {
    private final PersonRepository personRepository;
    @Override
    public List<PersonResponseDTO> getPeople() {
        return personRepository.findAll().stream()
                .map(PersonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PersonResponseDTO findPersonByTaxNumber(String taxNumber) {
            Optional<Person> person= personRepository.findById(taxNumber);
            if(person.isEmpty()){
                throw new PersonNotFoundException("Person not found with this Tax Number"+ taxNumber);
            }
            log.info("Person found with this tax number: {}", taxNumber);
            return PersonMapper.toResponseDTO(person.get());
    }

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO person) {
            if(personRepository.existsById(person.taxNumber())){
                throw new PersonAlreadyExistsException(person.taxNumber());
            }
            PersonValidator.validatePersonDTO(person,true);
            Person newPerson = PersonMapper.toEntity(person);
            Person savedPerson = personRepository.save(newPerson);
            log.info("Person created with tax number: {}", savedPerson.getTaxNumber());
            return PersonMapper.toResponseDTO(savedPerson);
    }

    @Override
    public PersonResponseDTO updatePerson(PersonRequestDTO person) {
        Optional<Person> personOptional = personRepository.findById(person.taxNumber());
        if (personOptional.isEmpty()) {
            throw new PersonNotFoundException("Person not found with this tax number" + person.taxNumber());
        }
        PersonValidator.validatePersonDTO(person, true);
        Person updatedPerson = personOptional.get();
        updatedPerson.setLastName(person.lastName());
        updatedPerson.setFirstName(person.firstName());
        updatedPerson.setDateOfBirth(person.dateOfBirth());
        Person savedPerson = personRepository.save(updatedPerson);
        log.info("Person updated with tax number: {}", updatedPerson.getTaxNumber());
        return PersonMapper.toResponseDTO(savedPerson);
    }

    @Override
    public void deletePerson(String taxNumber) {
            if(!personRepository.existsById(taxNumber)){
                throw new PersonNotFoundException("Person not found with this tax number"+ taxNumber );
            }
            personRepository.deleteById(taxNumber);
            log.info("Person with tax number {} deleted successfully", taxNumber);
    }

    @Override
    public List<PersonResponseDTO> findPeopleByNameAndAge(String name, LocalDate date) {
        if (name != null && !name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
            return Collections.emptyList();
        }
        LocalDate cutoffDate = LocalDate.now().minusYears(30);
        List<PersonResponseDTO> peopleFound = personRepository
                .findByNameStartingWithAndOlderThan(name, cutoffDate)
                .stream()
                .map(PersonMapper::toResponseDTO)
                .collect(Collectors.toList());
        return peopleFound.isEmpty() ? Collections.emptyList() : peopleFound;
    }
}
