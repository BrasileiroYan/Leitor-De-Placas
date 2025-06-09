package com.example.plateReader.repository;

import com.example.plateReader.model.ActivationToken;
import com.example.plateReader.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ActivationTokenRespository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    Optional<ActivationToken> findByUser(AppUser user);
}
