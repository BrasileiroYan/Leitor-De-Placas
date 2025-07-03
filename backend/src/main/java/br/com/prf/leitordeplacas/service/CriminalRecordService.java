package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.CriminalRecordResponseDTO;
import br.com.prf.leitordeplacas.model.CriminalRecord;
import br.com.prf.leitordeplacas.repository.CriminalRecordRepository;
import br.com.prf.leitordeplacas.service.exception.CriminalRecordNotFoundException;
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
