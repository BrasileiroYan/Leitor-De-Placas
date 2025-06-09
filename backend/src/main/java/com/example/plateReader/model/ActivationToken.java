package com.example.plateReader.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class ActivationToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", unique = true, nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private Instant expirationDate;

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Instant getExpiryDate() {
        return expirationDate;
    }

    public void setExpiryDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}
