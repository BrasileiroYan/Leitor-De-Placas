package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.ScanHistoryResponseDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.ScanHistory;
import br.com.prf.leitordeplacas.repository.ScanHistoryRepository;
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
