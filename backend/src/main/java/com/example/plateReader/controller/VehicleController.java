package com.example.plateReader.controller;

import com.example.plateReader.dto.CrimeResponseDTO;
import com.example.plateReader.dto.CriminalRecordResponseDTO;
import com.example.plateReader.dto.PersonResponseDTO;
import com.example.plateReader.dto.VehicleResponseDTO;
import com.example.plateReader.service.VehicleReportPdfService;
import com.example.plateReader.service.VehicleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleReportPdfService vehicleReportPdfService;

    public VehicleController(VehicleService vehicleService, VehicleReportPdfService vehicleReportPdfService) {
        this.vehicleService = vehicleService;
        this.vehicleReportPdfService = vehicleReportPdfService;
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDTO>> findAll() {
        List<VehicleResponseDTO> vehiclesDTO = vehicleService.findAll();

        return ResponseEntity.ok().body(vehiclesDTO);
    }

    @GetMapping(path = "/{plate}")
    public ResponseEntity<VehicleResponseDTO> findByPlate(@PathVariable String plate) {
        VehicleResponseDTO vehicleDTO = vehicleService.findByPlate(plate);

        return ResponseEntity.ok().body(vehicleDTO);
    }

    @GetMapping(path = "/{plate}/download-pdf")
    public ResponseEntity<byte[]> generateVehiclePdfReport(@PathVariable String plate) {
        byte[] pdfBytes = vehicleReportPdfService.generateVehicleReportPDF(plate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio_placa_" + plate + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping(path = "/{plate}/owner")
    public ResponseEntity<PersonResponseDTO> findOwnerByPlate(@PathVariable String plate) {
        PersonResponseDTO ownerDTO = vehicleService.findOwnerByPlate(plate);

        return ResponseEntity.ok().body(ownerDTO);
    }

    @GetMapping(path = "/{plate}/owner/criminal-record")
    public ResponseEntity<CriminalRecordResponseDTO> findCriminalRecordByPlate(@PathVariable String plate) {
        CriminalRecordResponseDTO criminalRecordDTO = vehicleService.findCriminalRecordByPlate(plate);

        return ResponseEntity.ok().body(criminalRecordDTO);
    }

    @GetMapping(path = "/{plate}/owner/criminal-record/crimes")
    public ResponseEntity<List<CrimeResponseDTO>> findAllCrimesByPlate(@PathVariable String plate) {
        List<CrimeResponseDTO> crimesDTO = vehicleService.findAllCrimesByPlate(plate);

        return ResponseEntity.ok().body(crimesDTO);
    }

    @GetMapping(path = "/{plate}/owner/criminal-record/crime/{id}")
    public ResponseEntity<CrimeResponseDTO> findCrimeByPlateAndId(@PathVariable String plate, @PathVariable Long id) {
        CrimeResponseDTO crimeDTO = vehicleService.findCrimeByPlateAndId(plate, id);

        return ResponseEntity.ok().body(crimeDTO);
    }
}
