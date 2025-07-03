package br.com.prf.leitordeplacas.repository;

import br.com.prf.leitordeplacas.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
}

