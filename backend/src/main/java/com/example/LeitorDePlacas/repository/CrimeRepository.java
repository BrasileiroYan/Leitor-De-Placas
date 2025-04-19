package com.example.LeitorDePlacas.repository;

import com.example.LeitorDePlacas.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CrimeRepository extends JpaRepository<Crime, Long> {

}
