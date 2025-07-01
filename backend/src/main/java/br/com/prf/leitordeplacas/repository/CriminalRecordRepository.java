package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.CriminalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CriminalRecordRepository extends JpaRepository<CriminalRecord, Long> {
}
