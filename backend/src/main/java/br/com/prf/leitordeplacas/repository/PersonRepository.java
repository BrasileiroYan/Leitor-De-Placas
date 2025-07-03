package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
