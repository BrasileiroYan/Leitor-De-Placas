package com.example.plateReader.repository;

import com.example.plateReader.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrimeRepository extends JpaRepository<Crime, Long> {

    Optional<Crime> findByCriminalRecord_Person_PlateAndId(String plate, Long id);

    List<Crime> findByCriminalRecord_Id(Long id);
}
