package com.example.plateReader.model.tokens;

import com.example.plateReader.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "app_user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(nullable = false)
    private Instant expiryDate;

    public Long getId() { return id; }
    public String getToken() { return token; }
    public Instant getExpiryDate() { return expiryDate; }
    public AppUser getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setToken(String token) { this.token = token; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
    public void setUser(AppUser user) { this.user = user; }
}
