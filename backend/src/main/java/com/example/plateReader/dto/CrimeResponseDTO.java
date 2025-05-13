package com.example.plateReader.dto;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.enums.CrimeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class CrimeResponseDTO {

    private Long id;
    private String crimeType;
    private String crimeDateTime;
    private String description;
    private CrimeStatus crimeStatus;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    public CrimeResponseDTO(Crime crime) {
        this.id = crime.getId();
        this.crimeType = crime.getCrimeType();
        this.crimeDateTime = crime.getCrimeDateTime().format(formatter);
        this.description = crime.getDescription();
        this.crimeStatus = crime.getCrimeStatus();
    }
}
