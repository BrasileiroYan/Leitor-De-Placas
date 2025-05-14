package com.example.plateReader.controller;

import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.model.CriminalRecord;
import com.example.plateReader.service.CriminalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/criminal-records")
public class CriminalRecordController {

    private final CriminalRecordService criminalRecordService;

    public CriminalRecordController(CriminalRecordService criminalRecordService) {
        this.criminalRecordService = criminalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<CriminalRecordResponseDTO>> findAll() {
        List<CriminalRecordResponseDTO> criminalRecordListDTO = criminalRecordService.findAll();
        return ResponseEntity.ok().body(criminalRecordListDTO);
    }
}
