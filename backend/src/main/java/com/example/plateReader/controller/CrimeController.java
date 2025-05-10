package com.example.plateReader.controller;

import com.example.plateReader.model.Crime;
import com.example.plateReader.service.CrimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/crime")
public class CrimeController {

    private final CrimeService crimeService;

    public CrimeController(CrimeService crimeRecordService) {
        this.crimeService = crimeRecordService;
    }

    @GetMapping
    public ResponseEntity<List<Crime>> findAll(){
        List<Crime> crimes = crimeService.findAll();
        return ResponseEntity.ok().body(crimes);
    }

    @GetMapping(path = "{plate}/{id}")
    public ResponseEntity<Crime> findByCriminalRecord_Person_PlateAndId(@PathVariable String plate, @PathVariable Long id){
        Crime specific = crimeService.findByCriminalRecord_Person_PlateAndId(plate, id);
        return ResponseEntity.ok().body(specific);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<List<Crime>> findByCriminalRecord_Id(@PathVariable Long id){
        List<Crime> personCrimes = crimeService.findByCriminalRecord_Id(id);
        return ResponseEntity.ok().body(personCrimes);
    }
}
