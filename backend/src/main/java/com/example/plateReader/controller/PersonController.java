package com.example.plateReader.controller;

import com.example.plateReader.dto.CrimeResponseDTO;
import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.dto.PersonResponseDTO;
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
@RequestMapping(value = "/owners")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personRecordService) {
        this.personService = personRecordService;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        List<PersonResponseDTO> peopleDTO = personService.findAll();
        return ResponseEntity.ok().body(peopleDTO);
    }
}
