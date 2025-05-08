package com.example.plateReader.service;

import com.example.plateReader.model.Person;
import com.example.plateReader.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonRecordService {

    private final PersonRepository personRecordRepository;

    public PersonRecordService(PersonRepository personRecordRepository) {
        this.personRecordRepository = personRecordRepository;
    }

    public List<Person> findAll() {
        return personRecordRepository.findAll();
    }

    public Person findByPlate(String plate) {
        Optional<Person> optionalPersonRecord = personRecordRepository.findByPlate(plate);
        return optionalPersonRecord.get();
    }
}
