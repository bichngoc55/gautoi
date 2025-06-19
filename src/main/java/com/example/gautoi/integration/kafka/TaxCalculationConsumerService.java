package com.example.gautoi.integration.kafka;

import com.example.gautoi.constant.KafkaConstants;
import com.example.gautoi.entity.Person;
import com.example.gautoi.entity.TaxCalculationEvent;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.TaxValidationException;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.validation.TaxEventValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxCalculationConsumerService {
    private final PersonRepository personRepository;
    @KafkaListener(topics = KafkaConstants.TAX_CALCULATION_TOPIC, groupId = KafkaConstants.TAX_GROUP, containerFactory = KafkaConstants.TAX_KAFKA_FACTORY)
    public void consumeTaxCalculation( TaxCalculationEvent taxEvent) {
        try{
            String taxNumber=taxEvent.getTaxNumber();
            Optional<Person> personFound= personRepository.findById(taxNumber);
            if(personFound.isEmpty()){
                throw new PersonNotFoundException("Person not found with this tax number"+ taxNumber);
            }
//            validation amount
            TaxEventValidation.TaxAmountValidation(taxEvent.getAmount());
            Person person=personFound.get();
            Double totalDebt =taxEvent.getAmount() + person.getTaxDebt();
            person.setTaxDebt(totalDebt);
            personRepository.save(person);
            log.info("Tax calculation after event with tax debt {}", person.getTaxDebt());
        } catch (KafkaException e) {
            log.error("Error while consuming tax calculation event", e);
        } catch (PersonNotFoundException | TaxValidationException e) {
            log.error(e.getMessage());
        }
    }
}
