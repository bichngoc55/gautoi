package com.example.gautoi.repository;

import com.example.gautoi.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    Page<Person> findByFirstNameStartingWithOrLastNameStartingWith(String firstnamePrefix, String lastnamePrefix, Pageable pageable);
}
