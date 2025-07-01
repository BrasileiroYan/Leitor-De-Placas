package br.com.prf.leitordeplacas.dto;

import java.time.Instant;

public record ScanHistoryResponseDTO(
        Long id,
        String scannedPlate,
        Instant scanTimestamp,
        String location
) {
}
