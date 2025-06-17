package com.example.plateReader.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import com.example.plateReader.model.enums.LicenseCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(of = "id")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter private String fullName;
    @Setter private String gender;
    @Setter private String rg;
    @Setter private String cpf;
    @Setter private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Setter private LicenseCategory licenseCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    @Setter private CriminalRecord criminalRecord;

    public Person() {
    }

    public Person(String fullName, String gender, String rg, String cpf, LocalDate birthDate, LicenseCategory licenseCategory) {
        this.fullName = fullName;
        this.gender = gender;
        this.rg = rg;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.licenseCategory = licenseCategory;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getRg() {
        return rg;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LicenseCategory getLicenseCategory() {
        return licenseCategory;
    }

    public CriminalRecord getCriminalRecord() {
        return criminalRecord;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }
}
