package com.example.LeitorDePlacas.model;

import java.io.Serializable;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FichaPessoa implements Serializable {

    @Id
    @GeneratedValue
    @Getter @Setter private Long id;
    @Getter @Setter private String placa;
    @Getter @Setter private String nomeCompleto;
    @Getter @Setter private String rg;
    @Getter @Setter private String cpf;
    @Getter @Setter private LocalDate dataNascimento;

    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL)
    private List<Crime> crimes = new ArrayList<>();
}
