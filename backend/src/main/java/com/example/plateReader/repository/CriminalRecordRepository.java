package com.example.plateReader.repository;

import com.example.plateReader.model.CriminalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CriminalRecordRepository extends JpaRepository<CriminalRecord, Long> {
}
