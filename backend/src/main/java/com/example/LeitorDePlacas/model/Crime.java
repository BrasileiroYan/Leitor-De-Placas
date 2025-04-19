package com.example.LeitorDePlacas.model;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
public class Crime {

    @Id @GeneratedValue
    @Getter private Long id;
    @Getter @Setter private String placa;
    @Getter @Setter private String crime;
    @Getter @Setter private Date dataCrime;
    @Getter @Setter private String descricao;

    @ManyToOne
    private FichaPessoa ficha;
}
