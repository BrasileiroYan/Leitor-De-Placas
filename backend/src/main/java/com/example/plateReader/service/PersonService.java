package com.example.plateReader.service;

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
}
