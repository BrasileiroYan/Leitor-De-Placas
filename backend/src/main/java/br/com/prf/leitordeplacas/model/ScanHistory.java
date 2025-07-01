package br.com.prf.leitordeplacas.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class ScanHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String scannedPlate;

    @Column(nullable = false)
    private Instant scanTimestamp;

    private String location; // Ex: "BR-116, Km 50, Fortaleza-CE"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
}
