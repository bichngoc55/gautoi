package com.example.gautoi.controller;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.exception.PersonAlreadyExistsException;
import com.example.gautoi.exception.PersonNotFoundException;
import com.example.gautoi.exception.PersonValidationException;
import com.example.gautoi.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
@Slf4j
public class PersonController {
    private final PersonService personService;
    @GetMapping()
    public ResponseEntity<List<PersonResponseDTO>> getPeople(){
        log.info("getPeople");
        List<PersonResponseDTO> allPeople = personService.getPeople();
        return ResponseEntity.status(HttpStatus.OK).body(allPeople);
    }

    @PostMapping()
    public ResponseEntity<Object> createPerson( @RequestBody PersonRequestDTO person) {
       try{
           log.info("createPerson");
           PersonResponseDTO personResponseDTO = personService.createPerson(person);
           return ResponseEntity.status(HttpStatus.CREATED).body(personResponseDTO);
       }catch (PersonValidationException | PersonAlreadyExistsException e){
           log.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }
    @PutMapping()
    public ResponseEntity<Object> updatePersonByTaxNumber(@RequestBody PersonRequestDTO person){
        try {
            log.info("updatePersonByTaxNumber");
            PersonResponseDTO personResponseDTO = personService.updatePerson(person);
            return ResponseEntity.ok(personResponseDTO);
        } catch (PersonNotFoundException | PersonValidationException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }
    @DeleteMapping("/{taxNumber}")
    public ResponseEntity<String> deletePerson(@PathVariable  String taxNumber){
        try {
            log.info("deletePerson");
            personService.deletePerson(taxNumber);
            return ResponseEntity.ok("Deleted person with tax number " + taxNumber + " successfully");
        }   catch ( PersonNotFoundException e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{taxNumber}")
    public ResponseEntity<Object> findByTaxNumber(@PathVariable String taxNumber){
        try {
            PersonResponseDTO person = personService.findPersonByTaxNumber(taxNumber);
            return ResponseEntity.ok(person);
        }
        catch (PersonNotFoundException e){
        log.error("Person with this tax number {} not found", taxNumber);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    }
    @GetMapping("/search")
    public ResponseEntity<List<PersonResponseDTO>> findPeopleByNameAndAge(@RequestParam String name, @RequestParam LocalDate date){
        List<PersonResponseDTO> people = personService.findPeopleByNameAndAge(name,date);
        return ResponseEntity.ok(people);
    }

}