package br.com.prf.leitordeplacas.repository.tokens;

import br.com.prf.leitordeplacas.model.tokens.ActivationToken;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ActivationTokenRespository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    Optional<ActivationToken> findByUser(AppUser user);
}
