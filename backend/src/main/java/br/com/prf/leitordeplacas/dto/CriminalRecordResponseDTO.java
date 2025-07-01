package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.model.CriminalRecord;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CriminalRecordResponseDTO {

    private List<CrimeResponseDTO> crimesList = new ArrayList<>();

    public CriminalRecordResponseDTO(CriminalRecord criminalRecord) {
        setCrimesListFromCriminalRecord(criminalRecord);
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
