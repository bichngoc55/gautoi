package com.example.gautoi.service;

import com.example.gautoi.annotation.AuditableLog;
import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.PersonValidationException;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.util.SourceType;
import com.example.gautoi.validation.PersonEventValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    @AuditableLog(SourceType.SERVICE)
    @Override
    public List<PersonResponseDTO> getPeople() {
        return personRepository.findAll().stream()
                .map(PersonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    @AuditableLog(SourceType.SERVICE)
    @Override
    public PersonResponseDTO findPersonByTaxNumber(String taxNumber) {
        Optional<Person> person= personRepository.findById(taxNumber);
        if(person.isEmpty()){
            throw new PersonNotFoundException("Person not found with this Tax Number " + taxNumber);
        }
        log.info("Person found with this tax number: {}", taxNumber);
        return PersonMapper.toResponseDTO(person.get());
    }
    @AuditableLog(SourceType.SERVICE)
    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO person) {
        if(personRepository.existsById(person.taxNumber())){
            throw new PersonAlreadyExistsException(person.taxNumber());
        }
        PersonEventValidation.validatePersonDTO(person,true);
        Person newPerson = PersonMapper.toEntity(person);
        Person savedPerson = personRepository.save(newPerson);
        log.info("Person created with tax number: {}", savedPerson.getTaxNumber());
        return PersonMapper.toResponseDTO(savedPerson);
    }
    @AuditableLog(SourceType.SERVICE)
    @Override
    public PersonResponseDTO updatePerson(PersonRequestDTO person) {
        Optional<Person> personOptional = personRepository.findById(person.taxNumber());
        if (personOptional.isEmpty()) {
            throw new PersonNotFoundException("Person not found with this tax number " + person.taxNumber());
        }
        PersonEventValidation.validatePersonDTO(person, true);
        Person updatedPerson = personOptional.get();
        updatedPerson.setLastName(person.lastName());
        updatedPerson.setFirstName(person.firstName());
        updatedPerson.setDateOfBirth(person.dateOfBirth());
        Person savedPerson = personRepository.save(updatedPerson);
        log.info("Person updated with tax number: {}", updatedPerson.getTaxNumber());
        return PersonMapper.toResponseDTO(savedPerson);
    }
    @AuditableLog(SourceType.SERVICE)
    @Override
    public void deletePerson(String taxNumber) {
        if(!personRepository.existsById(taxNumber)){
            throw new PersonNotFoundException("Person not found with this tax number " + taxNumber);
        }
        personRepository.deleteById(taxNumber);
        log.info("Person with tax number {} deleted successfully", taxNumber);
    }
    @AuditableLog(SourceType.SERVICE)
    @Override
    public Page<PersonResponseDTO> findPeopleByNameAndAge(String name, int minAge, Pageable pageable) {
        if (name != null && !name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
            throw new PersonValidationException("Name must be required and upper case letter.");
        }
        if (minAge < 0) {
            throw new PersonValidationException("Minimum age cannot be negative.");
        }
        LocalDate today = LocalDate.now();
        Page<Person> peopleFound = personRepository.findByFirstNameStartingWithOrLastNameStartingWith(name, name, pageable);

        List<PersonResponseDTO> peopleResult = peopleFound.stream().filter(p -> Period.between(p.getDateOfBirth(), today).getYears() > minAge)
                .map(PersonMapper::toResponseDTO).toList();
        return new PageImpl<>(peopleResult);

    }
}
