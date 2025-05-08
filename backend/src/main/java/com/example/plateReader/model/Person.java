package com.example.plateReader.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Setter private String plate;

    @Setter private String fullName;
    @Setter private String rg;
    @Setter private String cpf;
    @Setter private LocalDate birthDate;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    @Setter private CriminalRecord criminalRecord;

    public Person(String plate, String fullName, String rg, String cpf, LocalDate birthDate) {
        this.plate = plate;
        this.fullName = fullName;
        this.rg = rg;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }
}
