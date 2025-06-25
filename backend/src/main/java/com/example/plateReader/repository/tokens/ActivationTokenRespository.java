package com.example.plateReader.repository.tokens;

import com.example.plateReader.model.tokens.ActivationToken;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ActivationTokenRespository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    Optional<ActivationToken> findByUser(AppUser user);
}
