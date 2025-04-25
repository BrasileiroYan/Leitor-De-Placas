package com.example.LeitorDePlacas.model;

import jakarta.persistence.*;
import lombok.*;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Crime implements Serializable {

    @Id
    @GeneratedValue
    @Getter private Long id;
    @Getter @Setter private String placa;
    @Getter @Setter private String crime;
    @Getter @Setter private LocalDateTime dataCrime;
    @Getter @Setter private String descricao;

    @ManyToOne
    private FichaPessoa ficha;
}
