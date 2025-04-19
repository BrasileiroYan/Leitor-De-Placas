package com.example.LeitorDePlacas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
public class User {

    @Id @GeneratedValue
    @Getter private Long id;
    @Getter @Setter private String login;
    @Getter @Setter private String senha;
}
