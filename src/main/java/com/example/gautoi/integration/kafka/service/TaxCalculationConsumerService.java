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
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxCalculationConsumerService {
    private final PersonRepository personRepository;

    public void consumeTaxBatchCalculation(ConsumerRecords<String, TaxCalculationEvent> taxRecords) {
        Set<String> taxNumbers = new HashSet<>();
        for (ConsumerRecord<String, TaxCalculationEvent> record : taxRecords) {
            TaxCalculationEvent event = record.value();
            if (event != null && event.getTaxNumber() != null) {
                taxNumbers.add(event.getTaxNumber());
            }
        }
        List<Person> people = personRepository.findAllById(taxNumbers);
        Map<String, Person> peopleMap = people.stream().collect(Collectors.toMap(Person::getTaxNumber, p -> p));
        for (ConsumerRecord<String, TaxCalculationEvent> record : taxRecords) {
            TaxCalculationEvent taxCalculationEvent = record.value();
            log.info("Tax Calculation Event: {}", taxCalculationEvent);
            String taxNumber = taxCalculationEvent.getTaxNumber();
            TaxEventValidation.validateTaxEvent(taxCalculationEvent.getAmount());
            Person person = peopleMap.get(taxNumber);
            if (person == null) {
                throw new PersonNotFoundException(taxNumber);
            }
            if (taxCalculationEvent.getAmount() == 9999.0) {
                throw new MaximumAmountExceedException("Amount is too large", record);
            }
            person.setTaxDebt(taxCalculationEvent.getAmount() + person.getTaxDebt());
            log.info("Tax calculation after event with tax debt {} ", person.getTaxDebt());
        }
        personRepository.saveAll(peopleMap.values());

    }
}
