package com.example.plateReader.dto;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.enums.CrimeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class CrimeResponseDTO {

    private String crimeType;
    private String crimeDateTime;
    private String description;
    private CrimeStatus crimeStatus;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    public CrimeResponseDTO(Crime crime) {
        this.crimeType = crime.getCrimeType();
        this.crimeDateTime = crime.getCrimeDateTime().format(formatter);
        this.description = crime.getDescription();
        this.crimeStatus = crime.getCrimeStatus();
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

    public CrimeStatus getCrimeStatus() {
        return crimeStatus;
    }
}
