package com.example.plateReader.model;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String plate;
    private String fullName;
    private String rg;
    private String cpf;
    private LocalDate birthDate;

    @JsonIgnore
    @OneToMany(mappedBy = "personRecord", cascade = CascadeType.ALL)
    private List<Crime> crimes = new ArrayList<>();
}
