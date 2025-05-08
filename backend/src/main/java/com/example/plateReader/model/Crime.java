package com.example.plateReader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Crime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter private String crimeType;
    @Setter private LocalDateTime crimeDateTime;
    @Setter private String description;

    @ManyToOne
    @JoinColumn(name = "criminal_record_id")
    private CriminalRecord criminalRecord;

    public Crime(String crimeType, LocalDateTime crimeDateTime, String description) {
        this.crimeType = crimeType;
        this.crimeDateTime = crimeDateTime;
        this.description = description;
    }

    public void setCriminalRecord(CriminalRecord criminalRecord) {
        this.criminalRecord = criminalRecord;
    }
}
