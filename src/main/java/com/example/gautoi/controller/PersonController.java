package com.example.gautoi.controller;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getPeople() {
        log.info("getPeople");
        List<PersonResponseDTO> allPeople = personService.getPeople();
        return ResponseEntity.ok(allPeople);
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody PersonRequestDTO person) {
        log.info("createPerson");
        PersonResponseDTO personResponseDTO = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personResponseDTO);
    }

    @PutMapping
    public ResponseEntity<PersonResponseDTO> updatePersonByTaxNumber(@RequestBody PersonRequestDTO person) {
        log.info("updatePersonByTaxNumber");
        PersonResponseDTO personResponseDTO = personService.updatePerson(person);
        return ResponseEntity.ok(personResponseDTO);
    }

    @DeleteMapping("/{taxNumber}")
    public ResponseEntity<String> deletePerson(@PathVariable String taxNumber) {
        log.info("deletePerson");
        personService.deletePerson(taxNumber);
        return ResponseEntity.ok("Deleted person with tax number " + taxNumber + " successfully");
    }

    @GetMapping("/{taxNumber}")
    public ResponseEntity<PersonResponseDTO> findByTaxNumber(@PathVariable String taxNumber) {
        log.info("findByTaxNumber {}", taxNumber);
        PersonResponseDTO person = personService.findPersonByTaxNumber(taxNumber);
        return ResponseEntity.ok(person);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PersonResponseDTO>> findPeopleByNameAndAge(
            @RequestParam String name,
            @RequestParam int minAge,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        List<PersonResponseDTO> people = personService.findPeopleByNameAndAge(name, minAge, offset, limit);
        return ResponseEntity.ok(people);
    }


}