package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.CrimeResponseDTO;
import br.com.prf.leitordeplacas.repository.CrimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CrimeService {

    private final CrimeRepository crimeRepository;

    public CrimeService(CrimeRepository crimeRepository) {
        this.crimeRepository = crimeRepository;
    }

    public List<CrimeResponseDTO> findAll() {
        return crimeRepository.findAll()
                .stream().map(CrimeResponseDTO::new)
                .collect(Collectors.toList());
    }
}
