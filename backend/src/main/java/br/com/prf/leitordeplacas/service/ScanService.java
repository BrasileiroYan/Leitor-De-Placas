package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.ScanHistoryResponseDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.ScanHistory;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import br.com.prf.leitordeplacas.repository.ScanHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ScanService {

    private final ScanHistoryRepository scanHistoryRepository;
    private final AppUserRepository appUserRepository;

    public ScanService(ScanHistoryRepository scanHistoryRepository, AppUserRepository appUserRepository) {
        this.scanHistoryRepository = scanHistoryRepository;
        this.appUserRepository = appUserRepository;
    }

    public void registerNewScan(String plate, AppUser user) {
        if (plate == null || plate.isBlank()) {
            return;
        }

        ScanHistory newScan = new ScanHistory();
        newScan.setScannedPlate(plate.toUpperCase());
        newScan.setUser(user);
        newScan.setScanTimestamp(Instant.now());

        scanHistoryRepository.save(newScan);
    }

    @Transactional(readOnly = true)
    public Page<ScanHistoryResponseDTO> getRecentScansForUser(int page, int size) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        AppUser currentUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Pageable pageable = PageRequest.of(page, size);
        Page<ScanHistory> historyPage = scanHistoryRepository.findByUserOrderByScanTimestampDesc(currentUser, pageable);
        return historyPage.map(scan -> new ScanHistoryResponseDTO(
                scan.getId(),
                scan.getScannedPlate(),
                scan.getScanTimestamp()
        ));
    }
}
