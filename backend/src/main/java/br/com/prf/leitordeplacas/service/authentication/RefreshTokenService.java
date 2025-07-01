package br.com.prf.leitordeplacas.service.authentication;

import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.tokens.RefreshToken;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import br.com.prf.leitordeplacas.repository.tokens.RefreshTokenRepository;
import br.com.prf.leitordeplacas.service.exception.AppUserNotFoundException;
import br.com.prf.leitordeplacas.service.exception.authentication.RefreshTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken createRefreshToken(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException(username));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpirationDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpirationDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "O refresh token expirou. Por favor, faça login novamente.");
        }
        return token;
    }
}