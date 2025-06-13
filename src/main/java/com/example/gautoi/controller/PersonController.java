package com.example.gautoi.controller;

import com.example.gautoi.dto.PersonRequestDTO;
import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;
    // REST ENDPOINT
    @GetMapping()
    public ResponseEntity<List<PersonResponseDTO>> getPeople(){
        List<PersonResponseDTO> allPeople = personService.getPeople();
        return ResponseEntity.status(HttpStatus.OK).body(allPeople);
    }

    @PostMapping()
    public ResponseEntity<PersonResponseDTO> createPerson( @RequestBody  PersonRequestDTO person) {
        PersonResponseDTO personResponseDTO = personService.createPerson(person);
        return new ResponseEntity<>(personResponseDTO, HttpStatus.CREATED);
    }
    @PutMapping()
    public ResponseEntity<PersonResponseDTO> updatePersonByTaxNumber(@RequestBody PersonRequestDTO person){
    // Requirement: tra ve status + data newly created va request gom full entity
        PersonResponseDTO personResponseDTO = personService.updatePerson(person);
//        return new ResponseEntity<>(personResponseDTO, HttpStatus.OK); khac nhau gi z?
        return ResponseEntity.status(HttpStatus.OK).body(personResponseDTO);
    }

// tim hieu sau ve response entity
    @DeleteMapping("/{taxNumber}")
    public ResponseEntity<String> deletePerson(@PathVariable String taxNumber){
    // Requirement: tra ve status 200 va message neu thanh cong
        personService.deletePerson(taxNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted person with tax number {} successfully");
    }
    // FIND BY TAX Number
    @GetMapping("/{taxNumber}")
    public ResponseEntity<PersonResponseDTO> findByTaxNumber(@PathVariable String taxNumber){
        PersonResponseDTO person  = personService.findPersonByTaxNumber(taxNumber);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }
// find by name and age
    @GetMapping("/search")
    public ResponseEntity<List<PersonResponseDTO>> findPeopleByNameAndAge(@RequestParam String name){
        List<PersonResponseDTO> people = personService.findPeopleByNameAndAge(name);
        return ResponseEntity.status(HttpStatus.OK).body(people);
    }

}
