package com.example.gautoi.integration.kafka;
import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.entity.Person;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.PersonValidationException;
import com.example.gautoi.entity.PersonEvent;
import com.example.gautoi.mapper.PersonMapper;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.validation.PersonEventValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.kafka.common.KafkaException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonConsumerService {
    private final PersonRepository personRepository;
// refactor code lai - after demo
    @KafkaListener(topics= KafkaConstants.PERSON_TOPIC, groupId = KafkaConstants.GROUP_ID, containerFactory = KafkaConstants.GROUP_KAFKA_FACTORY )
    public void consumePersonService(PersonEvent person) {
        try{
            switch (person.getEventType()){
                case CREATE -> handleCreatePersonEvent(person);
                case UPDATE -> handleUpdatePersonEvent(person);
                case DELETE -> handleDeletePersonEvent(person);
                default -> throw new IllegalArgumentException("Unexpected value: " + person.getEventType());
            }
        }catch (IllegalArgumentException  | PersonValidationException e) {
            log.error("Validation error processing person event: {} with this message {}", person, e.getMessage());

        } catch (PersonNotFoundException | PersonAlreadyExistsException e) {
            log.error("Business error: {}", e.getMessage(), e);
        } catch (SerializationException e) {
            log.error("Kafka message serialization/deserialization failed: {}", e.getMessage());
        } catch (KafkaException e) {
            log.error("Kafka client error occurred: {}", e.getMessage());
        }
    }

    private void handleDeletePersonEvent(PersonEvent personEvent) {
        PersonRequestDTO personDTO = personEvent.getPerson();
        String taxNumber = personDTO.taxNumber();
        if (!personRepository.existsById(taxNumber)) {
            throw new PersonNotFoundException("Person not found: " + taxNumber);
        }
        PersonEventValidation.validatePersonDTO(personDTO,false);
        personRepository.deleteById(taxNumber);
        log.info("Deleted person with tax number: {}", taxNumber);
    }

    private void handleUpdatePersonEvent(PersonEvent personEvent) {
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

    private void handleCreatePersonEvent(PersonEvent personEvent) {
        log.info("Creating person: {}", personEvent.getPerson());
        PersonRequestDTO personDTO = personEvent.getPerson();
        if (personRepository.existsById(personDTO.taxNumber())) {
            throw new PersonAlreadyExistsException("Tax number already exists: " + personDTO.taxNumber());
        }
        PersonEventValidation.validatePersonDTO(personDTO,true);
        Person newPerson = PersonMapper.toEntity(personDTO);
        personRepository.save(newPerson);
        log.info("Created person with tax number: {} and with body: {}", personDTO.taxNumber(), personDTO);
    }
}
