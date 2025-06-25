package com.example.gautoi.repository;

import com.example.gautoi.entity.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    List<Person> findByFirstNameStartingWithOrLastNameStartingWith(String firstnamePrefix, String lastnamePrefix, Pageable pageable);
}
