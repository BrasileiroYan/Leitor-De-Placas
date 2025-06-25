package com.example.plateReader.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OcrResponseDTO {

    private String plate;

    public OcrResponseDTO(String plate) {
        this.plate = plate;
    }

    public String getPlate() {
        return plate;
    }
}
