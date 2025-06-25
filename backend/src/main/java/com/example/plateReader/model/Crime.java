package com.example.plateReader.model;

import com.example.plateReader.model.enums.CrimeStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@EqualsAndHashCode(of = "id")
public class Crime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter private String crimeType;
    @Setter private LocalDateTime crimeDateTime;
    @Setter private String description;

    @Enumerated(EnumType.STRING)
    @Setter private CrimeStatus crimeStatus;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "criminal_record_id")
    private CriminalRecord criminalRecord;

    public Crime() {
    }

    public Crime(String crimeType, LocalDateTime crimeDateTime, String description, CrimeStatus crimeStatus) {
        this.crimeType = crimeType;
        this.crimeDateTime = crimeDateTime;
        this.description = description;
        this.crimeStatus = crimeStatus;
    }

    public Long getId() {
        return id;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public LocalDateTime getCrimeDateTime() {
        return crimeDateTime;
    }

    public String getDescription() {
        return description;
    }

    public CrimeStatus getCrimeStatus() {
        return crimeStatus;
    }

    public CriminalRecord getCriminalRecord() {
        return criminalRecord;
    }

    public void setCriminalRecord(CriminalRecord criminalRecord) {
        this.criminalRecord = criminalRecord;
    }
}
