package com.example.plateReader.service;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.service.exception.CrimeNotFoundException;
import com.example.plateReader.service.exception.CriminalRecordNotFoundException;
import com.example.plateReader.service.exception.PersonNotFoundException;
import com.example.plateReader.model.Person;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRecordRepository) {
        this.personRepository = personRecordRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByPlate(String plate) {
        return personRepository.findByPlate(plate).orElseThrow(() -> new PersonNotFoundException(plate));
    }

    public CriminalRecord findCriminalRecordByPlate(String plate) {
        Person person = personRepository.findByPlate(plate).
                orElseThrow(() -> new PersonNotFoundException(plate));

        CriminalRecord criminalRecord = person.getCriminalRecord();
        if (criminalRecord == null) {
            throw new CriminalRecordNotFoundException("Ficha Criminal não encontrada para placa [" + person.getPlate() + "].");
        }

        return criminalRecord;
    }

    public List<Crime> findAllCrimesByPlate(String plate) {
        Person person = personRepository.findByPlate(plate).
                orElseThrow(() -> new PersonNotFoundException(plate));

        if (person.getCriminalRecord() == null) {
            throw new CriminalRecordNotFoundException("Ficha criminal não encontrada para placa [" + person.getPlate() + "].");
        }

        List<Crime> crimeList = person.getCriminalRecord().getCrimeList();

        if (crimeList == null || crimeList.isEmpty()) {
            throw new CrimeNotFoundException("Não há crimes encontrados para placa [" + person.getPlate() + "].");
        }

        return crimeList;
    }

    public Crime findCrimeByPlateAndId(String plate, Long id) {
        Person person = personRepository.findByPlate(plate).
                orElseThrow(() -> new PersonNotFoundException(plate));

        if (person.getCriminalRecord() == null) {
            throw new CriminalRecordNotFoundException("Ficha criminal não encontrada para placa [" + person.getPlate() + "].");
        }

        List<Crime> crimeList = person.getCriminalRecord().getCrimeList();

        if (crimeList == null || crimeList.isEmpty()) {
            throw new CrimeNotFoundException("Não há crimes encontrados para placa [" + person.getPlate() + "].");
        }

        Crime crime = crimeList.stream().filter(c -> c.getId().equals(id)).
                findFirst().orElseThrow(() -> new CrimeNotFoundException(id, plate));

        return crime;
    }
}
