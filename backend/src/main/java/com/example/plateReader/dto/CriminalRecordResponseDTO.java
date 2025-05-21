package com.example.plateReader.dto;

import com.example.plateReader.model.CriminalRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CriminalRecordResponseDTO {

    private Long id;
    private List<CrimeResponseDTO> crimesList = new ArrayList<>();

    public CriminalRecordResponseDTO(CriminalRecord criminalRecord) {
        this.id = criminalRecord.getId();
        setCrimesListFromCriminalRecord(criminalRecord);
    }

    public Long getId() {
        return id;
    }

    public List<CrimeResponseDTO> getCrimesList() {
        return crimesList;
    }

    private void setCrimesListFromCriminalRecord(CriminalRecord criminalRecord) {
        this.crimesList = criminalRecord.getCrimeList().stream()
                .map(CrimeResponseDTO::new).collect(Collectors.toList());
    }

    public void addCrime(CrimeResponseDTO crime) {
        crimesList.add(crime);
    }

    public void removeCrime(CrimeResponseDTO crime) {
        crimesList.remove(crime);
    }
}
