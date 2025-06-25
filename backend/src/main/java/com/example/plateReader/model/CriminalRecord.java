package com.example.plateReader.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(of = "id")
public class CriminalRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    @Setter private Person person;

    @OneToMany(mappedBy = "criminalRecord", cascade = CascadeType.ALL)
    private List<Crime> crimeList = new ArrayList<>();

    public CriminalRecord() {
    }

    public CriminalRecord(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public List<Crime> getCrimeList() {
        return crimeList;
    }

    public void addCrime(Crime crime) {
        if (!(crimeList.contains(crime))) {
            crimeList.add(crime);
            crime.setCriminalRecord(this);
        }
    }

    public void removeCrime(Crime crime) {
        crimeList.remove(crime);
        crime.setCriminalRecord(null);
    }
}
