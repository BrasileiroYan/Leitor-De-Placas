package com.example.plateReader.repository.tokens;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.tokens.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
