package com.example.gautoi.integration.kafka.service;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.entity.PersonEvent;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.SimulatePersonRunTimeException;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.validation.PersonEventValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PersonServiceKafka {
    private final PersonRepository personRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public  void handleDeletePersonEvent(PersonEvent personEvent) {
        log.info("handleDeletePersonEvent");
        PersonRequestDTO personDTO = personEvent.getPerson();
        String taxNumber = personDTO.taxNumber();
        if (!personRepository.existsById(taxNumber)) {
            throw new PersonNotFoundException("Person not found: " + taxNumber);
        }
        PersonEventValidation.validatePersonDTO(personDTO,false);
        personRepository.deleteById(taxNumber);
        log.info("Deleted person with tax number: {}", taxNumber);
    }

    public void handleUpdatePersonEvent(PersonEvent personEvent) {
        log.info("Updating person: {}", personEvent.getPerson());
        PersonRequestDTO personDTO = personEvent.getPerson();
        Optional<Person> personOpt = personRepository.findById(personDTO.taxNumber());
        if (personOpt.isEmpty()) {
            throw new PersonNotFoundException("Person not found: " + personDTO.taxNumber());
        }
        PersonEventValidation.validatePersonDTO(personDTO, true);
        Person existing = personOpt.get();
        existing.setFirstName(personDTO.firstName());
        existing.setLastName(personDTO.lastName());
        existing.setDateOfBirth(personDTO.dateOfBirth());
        personRepository.save(existing);
        log.info("Updated person with tax number: {}", personDTO.taxNumber());
    }

    public void handleCreatePersonEvent(ConsumerRecord<String, PersonEvent> personEvent) {
            PersonRequestDTO personDTO = personEvent.value().getPerson();
            log.info("Created person with   {}", personDTO);

        if (personRepository.existsById(personDTO.taxNumber())) {
            throw new PersonAlreadyExistsException("Tax number already exists: " + personDTO.taxNumber());
        }
            if (personDTO.taxNumber().equals("123456789"))
                throw new SimulatePersonRunTimeException("Simulate person runtime exception", personEvent);
            PersonEventValidation.validatePersonDTO(personDTO, true);
        Person newPerson = PersonMapper.toEntity(personDTO);
        personRepository.save(newPerson);
        log.info("Created person with tax number: {} and with body: {}", personDTO.taxNumber(), personDTO);

    }
}
