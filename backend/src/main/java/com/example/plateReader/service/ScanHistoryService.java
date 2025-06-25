package com.example.plateReader.service;

import com.example.plateReader.dto.ScanHistoryResponseDTO;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.ScanHistory;
import com.example.plateReader.repository.ScanHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScanHistoryService {

    private final ScanHistoryRepository scanHistoryRepository;

    public ScanHistoryService(ScanHistoryRepository scanHistoryRepository) {
        this.scanHistoryRepository = scanHistoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ScanHistoryResponseDTO> getRecentScansForUser(AppUser user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ScanHistory> historyPage = scanHistoryRepository.findByUserOrderByScanTimestampDesc(user, pageable);

        return historyPage.map(scan -> new ScanHistoryResponseDTO(
                scan.getId(),
                scan.getScannedPlate(),
                scan.getScanTimestamp(),
                scan.getLocation()
        ));
    }
}
