package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.Crime;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CrimeRepository extends JpaRepository<Crime, Long> {
}
