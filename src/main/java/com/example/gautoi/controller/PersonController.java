package com.example.gautoi.controller;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    @GetMapping()
    public ResponseEntity<List<PersonResponseDTO>> getPeople(){
        List<PersonResponseDTO> allPeople = personService.getPeople();
        return ResponseEntity.status(HttpStatus.OK).body(allPeople);
    }

    @PostMapping()
    public ResponseEntity<PersonResponseDTO> createPerson( @RequestBody PersonRequestDTO person) {
        PersonResponseDTO personResponseDTO = personService.createPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personResponseDTO);
    }
    @PutMapping()
    public ResponseEntity<PersonResponseDTO> updatePersonByTaxNumber(@RequestBody PersonRequestDTO person){
        PersonResponseDTO personResponseDTO = personService.updatePerson(person);
        return ResponseEntity.ok(personResponseDTO);
    }
// tim hieu sau ve response entity
    @DeleteMapping()
    public ResponseEntity<String> deletePerson(@RequestParam String taxNumber){
        personService.deletePerson(taxNumber);
        return ResponseEntity.ok("Deleted person with tax number " + taxNumber + " successfully");
    }
    @GetMapping("/{taxNumber}")
    public ResponseEntity<PersonResponseDTO> findByTaxNumber(@PathVariable String taxNumber){
        PersonResponseDTO person  = personService.findPersonByTaxNumber(taxNumber);
        return ResponseEntity.ok(person);
    }
    @GetMapping("/search")
    public ResponseEntity<List<PersonResponseDTO>> findPeopleByNameAndAge(@RequestParam String name, @RequestParam LocalDate date){
        List<PersonResponseDTO> people = personService.findPeopleByNameAndAge(name,date);
        return ResponseEntity.ok(people);
    }

}
