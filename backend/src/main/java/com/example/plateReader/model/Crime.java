package com.example.plateReader.model;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Crime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String crimeType;
    private LocalDateTime crimeDateTime;
    private String description;

    @ManyToOne
    @JoinColumn(name = "plate", referencedColumnName = "plate")
    private PersonRecord personRecord;
}
