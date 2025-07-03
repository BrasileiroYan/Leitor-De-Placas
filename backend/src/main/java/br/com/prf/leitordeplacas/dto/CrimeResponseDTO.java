package br.com.prf.leitordeplacas.dto;

import br.com.prf.leitordeplacas.model.Crime;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class CrimeResponseDTO {

    private String crimeType;
    private String crimeDateTime;
    private String description;
    private String crimeStatus;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    public CrimeResponseDTO(Crime crime) {
        this.crimeType = crime.getCrimeType();
        this.crimeDateTime = crime.getCrimeDateTime().format(formatter);
        this.description = crime.getDescription();
        this.crimeStatus = crime.getCrimeStatus().getDisplayName();
    }

    public String getCrimeType() {
        return crimeType;
    }

    public String getCrimeDateTime() {
        return crimeDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getCrimeStatus() {
        return crimeStatus;
    }
}
