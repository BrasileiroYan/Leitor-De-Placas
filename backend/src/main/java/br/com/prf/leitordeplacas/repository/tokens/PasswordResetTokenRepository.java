package br.com.prf.leitordeplacas.repository.tokens;

import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.tokens.PasswordResetToken;
import br.com.prf.leitordeplacas.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    List<PasswordResetToken> findByUser(AppUser user);
}
