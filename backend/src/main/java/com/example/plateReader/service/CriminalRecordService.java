package com.example.plateReader.service;

import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.repository.CriminalRecordRepository;
import com.example.plateReader.service.exception.CriminalRecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CriminalRecordService {

    private final CriminalRecordRepository criminalRecordRepository;

    public CriminalRecordService(CriminalRecordRepository criminalRecordRepository) {
        this.criminalRecordRepository = criminalRecordRepository;
    }

    public List<CriminalRecordResponseDTO> findAll() {
        return criminalRecordRepository.findAll()
                .stream().map(CriminalRecordResponseDTO::new)
                .collect(Collectors.toList());
    }

    public CriminalRecordResponseDTO findById(Long id) {
        CriminalRecord criminalRecord = criminalRecordRepository.findById(id).orElseThrow(() -> new CriminalRecordNotFoundException(id));

        return new CriminalRecordResponseDTO(criminalRecord);
    }
}
