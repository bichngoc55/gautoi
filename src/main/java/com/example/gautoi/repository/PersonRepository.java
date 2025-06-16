package com.example.gautoi.repository;


import com.example.gautoi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
//    @Query("SELECT p FROM Person p WHERE (p.firstName LIKE :name% OR p.lastName LIKE :name%) AND p.dateOfBirth < :date")
//    List<Person> findByNameStartingWithAndOlderThan(@Param("name") String name, @Param("date") LocalDate date);
    @Query("SELECT p FROM Person p WHERE (p.firstName LIKE CONCAT(:name, '%') OR p.lastName LIKE CONCAT(:name, '%')) AND p.dateOfBirth < :date")
    List<Person> findByNameStartingWithAndOlderThan(@Param("name") String name, @Param("date") LocalDate date);
}
