package com.example.plateReader.repository;

import com.example.plateReader.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CrimeRepository extends JpaRepository<Crime, Long> {
}
