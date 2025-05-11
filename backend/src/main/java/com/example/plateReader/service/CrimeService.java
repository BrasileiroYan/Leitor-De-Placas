package com.example.plateReader.service;

import com.example.plateReader.model.Crime;
import com.example.plateReader.repository.CrimeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrimeService {

    private final CrimeRepository crimeRepository;

    public CrimeService(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    public List<Crime> findAll() {
        return crimeRepository.findAll();
    }
}
