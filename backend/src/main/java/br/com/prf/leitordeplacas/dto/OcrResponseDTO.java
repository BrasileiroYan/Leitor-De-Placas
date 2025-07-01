package br.com.prf.leitordeplacas.dto;

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
