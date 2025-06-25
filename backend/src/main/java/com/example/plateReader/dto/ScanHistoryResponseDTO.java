package com.example.plateReader.dto;

import java.time.Instant;

public record ScanHistoryResponseDTO(
        Long id,
        String scannedPlate,
        Instant scanTimestamp,
        String location
) {
}
