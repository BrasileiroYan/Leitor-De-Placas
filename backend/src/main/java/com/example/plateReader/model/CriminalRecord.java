package com.example.plateReader.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class CriminalRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    @Setter private Person person;

    @OneToMany(mappedBy = "criminalRecord", cascade = CascadeType.ALL)
    private List<Crime> crimeList = new ArrayList<>();

    public CriminalRecord(Person person) {
        this.person = person;
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
