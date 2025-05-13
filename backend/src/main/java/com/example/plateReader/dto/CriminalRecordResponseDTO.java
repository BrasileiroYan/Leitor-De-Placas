package com.example.plateReader.dto;

import com.example.plateReader.model.CriminalRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CriminalRecordResponseDTO {

    @Setter private Long id;
    @Setter private PersonResponseDTO person;

    private List<CrimeResponseDTO> crimesList = new ArrayList<>();

    public CriminalRecordResponseDTO(CriminalRecord criminalRecord) {
        this.id = criminalRecord.getId();
        this.person = new PersonResponseDTO(criminalRecord.getPerson());

        setCrimesListFromCriminalRecord(criminalRecord);
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
