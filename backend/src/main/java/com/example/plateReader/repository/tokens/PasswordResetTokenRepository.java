package com.example.plateReader.repository.tokens;

import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.tokens.PasswordResetToken;
import com.example.plateReader.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(AppUser user);
}
