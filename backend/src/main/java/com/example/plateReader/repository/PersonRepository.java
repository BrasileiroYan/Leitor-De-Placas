package com.example.plateReader.repository;

import com.example.plateReader.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
