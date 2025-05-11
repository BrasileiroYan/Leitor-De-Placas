package com.example.plateReader.controller;

import com.example.plateReader.model.Crime;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.model.Person;
import com.example.plateReader.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/criminals")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personRecordService) {
        this.personService = personRecordService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> findAll() {
        List<Person> people = personService.findAll();
        return ResponseEntity.ok().body(people);
    }

    @GetMapping(path = "/{plate}")
    public ResponseEntity<Person> findByPlate(@PathVariable String plate) {
        Person person = personService.findByPlate(plate);
        return ResponseEntity.ok().body(person);
    }

    @GetMapping(path = "/{plate}/criminal-records")
    public ResponseEntity<CriminalRecord> findCriminalRecordByPlate(@PathVariable String plate) {
        CriminalRecord criminalRecord = personService.findCriminalRecordByPlate(plate);
        return ResponseEntity.ok().body(criminalRecord);
    }

    @GetMapping(path = "/{plate}/criminal-records/crimes")
    public ResponseEntity<List<Crime>> findAllCrimesByPlate(@PathVariable String plate) {
        List<Crime> crimeList = personService.findAllCrimesByPlate(plate);
        return ResponseEntity.ok().body(crimeList);
    }

    @GetMapping(path = "/{plate}/criminal-records/crimes/{id}")
    public ResponseEntity<Crime> findCrimeByPlateAndId(@PathVariable String plate, @PathVariable Long id) {
        Crime crime = personService.findCrimeByPlateAndId(plate, id);
        return ResponseEntity.ok().body(crime);
    }
}
