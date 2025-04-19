package com.example.LeitorDePlacas.model;

import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
public class FichaPessoa {

    @Id @GeneratedValue
    @Getter @Setter private Long id;
    @Getter @Setter private String placa;
    @Getter @Setter private String nomeCompleto;
    @Getter @Setter private String rg;
    @Getter @Setter private String cpf;
    @Getter @Setter private Date dataNascimento;

    @OneToMany(mappedBy = "ficha", cascade = CascadeType.ALL)
    private List<Crime> crimes = new ArrayList<>();
}
