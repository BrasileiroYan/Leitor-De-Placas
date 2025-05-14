package com.example.plateReader.controller;

import com.example.plateReader.dto.CrimeResponseDTO;
import com.example.plateReader.model.Crime;
import com.example.plateReader.service.CrimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/crimes")
public class CrimeController {

    private final CrimeService crimeService;

    public CrimeController(CrimeService crimeRecordService) {
        this.crimeService = crimeRecordService;
    }

    @GetMapping
    public ResponseEntity<List<CrimeResponseDTO>> findAll(){
        List<CrimeResponseDTO> crimesDTO = crimeService.findAll();

        return ResponseEntity.ok().body(crimesDTO);
    }
}
