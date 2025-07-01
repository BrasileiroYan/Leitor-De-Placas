package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.ScanHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanHistoryRepository extends JpaRepository<ScanHistory, Long> {

    Page<ScanHistory> findByUserOrderByScanTimestampDesc(AppUser user, Pageable pageable);
}
