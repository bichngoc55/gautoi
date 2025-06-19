package com.example.gautoi.repository;
import com.example.gautoi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
//    ko dung like nua
    @Query("SELECT p FROM Person p WHERE p.firstName LIKE :nameStart% OR p.lastName LIKE :nameStart%" )
    List<Person> findByNameStartingWithCaseSensitive(@Param("nameStart") String nameStart);

}
