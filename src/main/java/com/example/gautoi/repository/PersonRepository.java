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
    Optional<Person> findByTaxNumber(String taxNumber);
    boolean existsByTaxNumber(String taxNumber);
//  find people by name and age older than 30
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE :prefix% AND p.dateOfBirth < :date")
    Optional<List<Person>> findByPrefixAndOlderThan(@Param("prefix") String prefix, @Param("date") Integer data);
}
