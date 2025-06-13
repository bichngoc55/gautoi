package com.example.gautoi.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceImp implements PersonService {
    private final PersonRepository personRepository;
    private static final int AGE = 30;
    @Override
    public List<PersonResponseDTO> getPeople() {
            List<Person> people = personRepository.findAll();
            return people.stream().map(PersonMapper::toResponseDTO)
                    .collect(Collectors.toList());
    }

    @Override
    public PersonResponseDTO findPersonByTaxNumber(String taxNumber) {
        try{
            Person person = personRepository.findByTaxNumber(taxNumber)
                    .orElseThrow(() -> {
                        log.warn("Person not found with tax number: {}", taxNumber);
                        return new PersonNotFoundException("Person not found with tax number: " + taxNumber);
                    });
            log.debug("Successfully found person with tax number: {}", taxNumber);
            return PersonMapper.toResponseDTO(person);

        }catch(Exception e){
            log.error(e.getMessage());

        }
    }

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO person) {
        try{
            Optional<Person> existingPerson = personRepository.findByTaxNumber(person.taxNumber());
            if (existingPerson.isPresent()) {
                throw new PersonNotFoundException("Person already exists with tax number: " + person.taxNumber());
            }
            Person personCreated = PersonMapper.toEntity(person);
            personRepository.save(personCreated);
            return PersonMapper.toResponseDTO(personCreated);
        }
        catch(Exception e){
            log.error(e.getMessage());

        }

    }

    @Override
    public PersonResponseDTO updatePerson(PersonRequestDTO person) {
c       try{
            Person existingPerson = personRepository.findById(person.taxNumber())
                    .orElseThrow(() -> new PersonNotFoundException("Person not found with taxNumber: " + person.taxNumber()));
            if (!existingPerson.getTaxNumber().equals(person.taxNumber())) {
                throw new RuntimeException("Tax number cannot be updated");
            }
            if (person.firstName() != null && !person.firstName().isBlank()) {
                existingPerson.setFirstName(person.firstName());
            }
            if (person.lastName() != null && !person.lastName().isBlank()) {
                existingPerson.setLastName(person.firstName());
            }
            if(person.dateOfBirth() != null   ) {
                existingPerson.setDateOfBirth(person.dateOfBirth());
            }
            personRepository.save(existingPerson);
            log.info("Person updated: {} ", existingPerson.getTaxNumber());
            return PersonMapper.toResponseDTO(existingPerson);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePerson(String taxNumber) {
        try{
            if(!personRepository.existsByTaxNumber(taxNumber)){
                log.error("Tax number {} does not exist", taxNumber);
                return;
            }
            personRepository.deleteById(taxNumber);
            log.info("Person deleted successfully tax Number: {}", taxNumber);

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Override
    public List<PersonResponseDTO> findPeopleByNameAndAge(String name) {
         try{
            List<Person> peopleFound = personRepository.findByPrefixAndOlderThan(name,AGE).orElseThrow(()-> new PersonNotFoundException("Person not found with name and older than 30"));
            return peopleFound.stream().map(PersonMapper::toResponseDTO).collect(Collectors.toList());
        }catch(PersonNotFoundException e){
             log.error(e.getMessage());
        }
    }
}
