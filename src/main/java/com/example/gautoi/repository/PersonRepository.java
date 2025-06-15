package com.example.gautoi.repository;


import com.example.gautoi.dto.PersonResponseDTO;
import com.example.gautoi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, String> {
    @Query("SELECT p FROM Person p WHERE (p.firstName LIKE :name% OR p.lastName LIKE :name%) AND p.dateOfBirth < :date")
    List<PersonResponseDTO> findByNameStartingWithAndOlderThan(@Param("name") String name, @Param("date") LocalDate date);
}
