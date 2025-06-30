package com.example.gautoi.integration.kafka.service;

import com.example.gautoi.entity.Person;
import com.example.gautoi.entity.TaxCalculationEvent;
import com.example.gautoi.exception.MaximumAmountExceedException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.repository.PersonRepository;
import com.example.gautoi.validation.TaxEventValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxCalculationServiceKafka {
    private final PersonRepository personRepository;

    public void consumeTaxCalculation(ConsumerRecord<String, TaxCalculationEvent> taxRecord) {
        TaxCalculationEvent taxCalculationEvent = taxRecord.value();
        log.info("Tax Calculation Event: {}", taxCalculationEvent);
        String taxNumber = taxCalculationEvent.getTaxNumber();
            Optional<Person> personFound = personRepository.findById(taxNumber);
            if (personFound.isEmpty()) {
                throw new PersonNotFoundException("Person not found with this tax number hehhehehe" + taxNumber);
            }
        if (taxCalculationEvent.getAmount() == 9999.0) {
            throw new MaximumAmountExceedException("Amount is too large", taxRecord);
            }
        TaxEventValidation.validateTaxEvent(taxCalculationEvent.getAmount());
            Person person = personFound.get();
        Double totalDebt = taxCalculationEvent.getAmount() + person.getTaxDebt();
            person.setTaxDebt(totalDebt);
            personRepository.save(person);
        log.info("Tax calculation after event with tax debt {} ", person.getTaxDebt());
    }
}
