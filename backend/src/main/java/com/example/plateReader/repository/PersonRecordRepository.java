package com.example.plateReader.repository;

import com.example.plateReader.model.PersonRecord;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRecordRepository extends JpaRepository<PersonRecord, Long> {

}
