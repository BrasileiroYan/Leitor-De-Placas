package com.example.LeitorDePlacas.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"login", "senha"})
public class AppUser implements Serializable {

    @Id
    @GeneratedValue
    @Getter private Long id;
    @Getter @Setter private String login;
    @Getter @Setter private String senha;

}
