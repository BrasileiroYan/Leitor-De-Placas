package com.example.plateReader.controller;

import com.example.plateReader.dto.OcrResponseDTO;
import com.example.plateReader.service.OcrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/ocr")
public class OcrController {

    private final OcrService ocrService;

    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping(value = "/read")
    public ResponseEntity<OcrResponseDTO> readPlateFromImage(@RequestParam MultipartFile file) {
        String plate = ocrService.sendFileToPythonApi(file);

        return ResponseEntity.ok(new OcrResponseDTO(plate));
    }
}
