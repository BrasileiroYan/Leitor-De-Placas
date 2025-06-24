package com.example.plateReader.repository;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.ScanHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {

    Page<ScanHistory> findByUserOrderByScanTimestampDesc(AppUser user, Pageable pageable);
}
