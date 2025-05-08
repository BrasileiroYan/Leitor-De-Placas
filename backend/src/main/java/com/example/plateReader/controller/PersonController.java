package com.example.plateReader.controller;

import com.example.plateReader.model.Person;
import com.example.plateReader.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/person")
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
}
