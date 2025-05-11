package com.example.plateReader.service;

import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.repository.CriminalRecordRepository;
import com.example.plateReader.service.exception.CriminalRecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CriminalRecordService {

    private final CriminalRecordRepository criminalRecordRepository;

    public CriminalRecordService(CriminalRecordRepository criminalRecordRepository) {
        this.criminalRecordRepository = criminalRecordRepository;
    }

    public List<CriminalRecord> findAll() {
        return criminalRecordRepository.findAll();
    }

    public CriminalRecord findById(Long id) {
        return criminalRecordRepository.findById(id).orElseThrow(() -> new CriminalRecordNotFoundException(id));
    }
}
