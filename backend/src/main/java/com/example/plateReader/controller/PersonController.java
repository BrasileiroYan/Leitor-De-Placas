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
@RequestMapping(value = "/criminals")
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

    @GetMapping(path = "/{plate}")
    public ResponseEntity<PersonResponseDTO> findByPlate(@PathVariable String plate) {
        PersonResponseDTO personDTO  = personService.findByPlate(plate);
        return ResponseEntity.ok().body(personDTO);
    }

    @GetMapping(path = "/{plate}/criminal-records")
    public ResponseEntity<CriminalRecordResponseDTO> findCriminalRecordByPlate(@PathVariable String plate) {
        CriminalRecordResponseDTO criminalRecordDTO = personService.findCriminalRecordByPlate(plate);
        return ResponseEntity.ok().body(criminalRecordDTO);
    }

    @GetMapping(path = "/{plate}/criminal-records/crimes")
    public ResponseEntity<List<CrimeResponseDTO>> findAllCrimesByPlate(@PathVariable String plate) {
        List<CrimeResponseDTO> crimesDTO = personService.findAllCrimesByPlate(plate);
        return ResponseEntity.ok().body(crimesDTO);
    }

    @GetMapping(path = "/{plate}/criminal-records/crimes/{id}")
    public ResponseEntity<CrimeResponseDTO> findCrimeByPlateAndId(@PathVariable String plate, @PathVariable Long id) {
        CrimeResponseDTO crimeDTO = personService.findCrimeByPlateAndId(plate, id);
        return ResponseEntity.ok().body(crimeDTO);
    }
}
