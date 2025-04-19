package com.example.LeitorDePlacas.repository;

import com.example.LeitorDePlacas.model.FichaPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FichaPessoaRepository extends JpaRepository<FichaPessoa, Long> {

}
