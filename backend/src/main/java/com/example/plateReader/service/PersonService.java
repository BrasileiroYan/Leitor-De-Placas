package com.example.plateReader.service;

import com.example.plateReader.model.Person;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<Person> optionalPersonRecord = personRepository.findByPlate(plate);
        return optionalPersonRecord.get();
    }
}
