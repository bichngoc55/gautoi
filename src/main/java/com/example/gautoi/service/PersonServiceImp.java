package com.example.gautoi.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.PersonValidationException;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.util.ErrorUtil;
import com.example.gautoi.validation.PersonValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        try{
            Optional<Person> person= personRepository.findById(taxNumber);
            if(person.isEmpty()){
                throw new PersonNotFoundException("Person not found with this Tax Number"+ taxNumber);
            }
            log.info("Person found with this tax number: {}", taxNumber);
            return PersonMapper.toResponseDTO(person.get());
        }catch (PersonNotFoundException e){
            log.error("Person with this tax number {} not found", taxNumber);
            throw e;
        }
    }

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO person) {
        try{
            if(personRepository.existsById(person.taxNumber())){
                throw new PersonAlreadyExistsException("Person with this tax number already exist "+ person.taxNumber());
            }
            PersonValidator.validatePersonDTO(person,true);
            Person newPerson = PersonMapper.toEntity(person);
            Person savedPerson = personRepository.save(newPerson);
            log.info("Person created with tax number: {}", savedPerson.getTaxNumber());
            return PersonMapper.toResponseDTO(savedPerson);
        }catch (PersonAlreadyExistsException | PersonValidationException e){
            log.error(e.getMessage());
            throw e;
        }


    }

    @Override
    public PersonResponseDTO updatePerson(PersonRequestDTO person) {
        try{
            Optional<Person> personOptional = personRepository.findById(person.taxNumber());
            if(personOptional.isPresent()){
                throw new PersonNotFoundException("Person not found with this tax number"+ person.taxNumber());
            }
            PersonValidator.validatePersonDTO(person,true);
            Person updatedPerson = personOptional.get();
            updatedPerson.setLastName(person.lastName());
            updatedPerson.setFirstName(person.firstName());
            updatedPerson.setDateOfBirth(person.dateOfBirth());
            Person savedPerson = personRepository.save(updatedPerson);
            log.info("Person updated with tax number: {}", updatedPerson.getTaxNumber());
            return PersonMapper.toResponseDTO(savedPerson);
        }catch (PersonNotFoundException | PersonValidationException e){
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public void deletePerson(String taxNumber) {
        try{
            if(!personRepository.existsById(taxNumber)){
                throw new PersonNotFoundException("Person not found with this tax number"+ taxNumber );
            }
            personRepository.deleteById(taxNumber);
            log.info("Person with tax number {} deleted successfully", taxNumber);
        }
        catch (PersonNotFoundException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public List<PersonResponseDTO> findPeopleByNameAndAge(String name, LocalDate date) {
        if (name != null && !name.isEmpty() && Character.isLowerCase(name.charAt(0))) {
            return Collections.emptyList();
        }
        List<PersonResponseDTO> peopleFound = personRepository.findByNameStartingWithAndOlderThan(name,date);
        return peopleFound.isEmpty() ? Collections.emptyList() : peopleFound;
    }
}
