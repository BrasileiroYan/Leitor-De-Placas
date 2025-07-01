package br.com.prf.leitordeplacas.service.authentication;

import br.com.prf.leitordeplacas.dto.authentication.AuthRequestDTO;
import br.com.prf.leitordeplacas.dto.authentication.AuthResponseDTO;
import br.com.prf.leitordeplacas.model.tokens.ActivationToken;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.tokens.PasswordResetToken;
import br.com.prf.leitordeplacas.repository.tokens.ActivationTokenRespository;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import br.com.prf.leitordeplacas.repository.tokens.PasswordResetTokenRepository;
import br.com.prf.leitordeplacas.model.tokens.RefreshToken;
import br.com.prf.leitordeplacas.service.EmailService;
import br.com.prf.leitordeplacas.service.exception.AppUserNotFoundException;
import br.com.prf.leitordeplacas.service.exception.authentication.*;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long[] LOCKOUT_DURATION_MINUTES = {5, 15, 30, 60, 240, 1440}; // em minutos

    private final ActivationTokenRespository activationTokenRespository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EntityManager entityManager;

    public AuthService(ActivationTokenRespository activationTokenRespository, PasswordResetTokenRepository passwordResetTokenRepository, RefreshTokenService refreshTokenService, AppUserRepository appUserRepository, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, EntityManager entityManager) {
        this.activationTokenRespository = activationTokenRespository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.refreshTokenService = refreshTokenService;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.entityManager = entityManager;
    }

    @Transactional(noRollbackFor = {AuthenticationException.class, InvalidCredentialsException.class, AccountLockedException.class})
    public AuthResponseDTO authenticateUser(AuthRequestDTO authRequest) {
        AppUser user = null;

        try {

            // Lógica de Bloqueio/Desbloqueio -ANTES- de Autenticação
            user = appUserRepository.findByUsername(authRequest.getUsername())
                    .orElseThrow(() -> new AppUserNotFoundException(authRequest.getUsername()));

            if (user.isAccountLocked()) {  // Se a conta estiver bloqueada
                int index = Math.max(0, user.getConsecutiveLockouts() - 1);
                Long currentLockoutTime = LOCKOUT_DURATION_MINUTES[Math.min(index, LOCKOUT_DURATION_MINUTES.length - 1 )]; // Pega o minuto do vetor de acordo com consecutiveLockout

                Instant unlockTime = user.getLockTime().plus(currentLockoutTime, ChronoUnit.MINUTES);

                if (unlockTime.isAfter(Instant.now())) {  // Se o tempo de desbloqueio ainda não tiver chegado (estiver no futuro)
                    logger.warn("Tentativa de login para conta bloqueada: {}", user.getUsername());
                    throw new AccountLockedException("Sua conta está bloqueada por " + currentLockoutTime + " minutos. Tente novamente mais tarde.");
                } else {  // Tempo de bloqueio acabou, desbloquear conta
                    logger.info("Conta {} desbloqueada após fim do tempo de bloqueio.", user.getUsername());

                    user.setAccountLocked(false);
                    user.setFailedLoginAttempts(0);
                    user.setLockTime(null);
                    appUserRepository.save(user);
                }
            }

            // Lógica de Autenticação de usuário

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            // Lógica para sucesso de autenticação
            user.setAccountLocked(false);
            user.setFailedLoginAttempts(0);
            user.setLockTime(null);
            user.setConsecutiveLockouts(0);
            appUserRepository.save(user);

            logger.info("Login bem-sucedido para usuário: {}", user.getUsername());

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
            return new AuthResponseDTO(token, refreshToken.getToken());

        } catch (AuthenticationException e) {
            // Lógica para falha de autenticação


            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1); // Incrementa tentativas falhas

            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) { // Se atingir o max de tentativas
                user.setConsecutiveLockouts(user.getConsecutiveLockouts() + 1);
                user.setAccountLocked(true);
                user.setLockTime(Instant.now());

                int index = Math.max(0, user.getConsecutiveLockouts() - 1);
                Long actualLockoutTime = LOCKOUT_DURATION_MINUTES[Math.min(index, LOCKOUT_DURATION_MINUTES.length - 1 )]; // Pega o minuto do vetor de acordo com consecutiveLockout

                emailService.sendAccountLockedAlert(user.getUsername(), actualLockoutTime);
                logger.warn("Conta do usuário {} bloqueada devido a múltiplas tentativas falhas.", user.getUsername());

                appUserRepository.save(user);
                entityManager.flush();

                throw new AccountLockedException("Sua conta está bloqueada por " + actualLockoutTime + " minutos. Tente novamente mais tarde.");
            } else {
                appUserRepository.save(user);
            }

            throw new InvalidCredentialsException("Credenciais inválidas");
        }
    }

    @Transactional
    public void initiateUserActivation(String username) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException(username));

        if (user.isEnabled()) {
            throw new IllegalStateException("Usuário já está ativo");
        }

        activationTokenRespository.findByUser(user).ifPresent(activationTokenRespository::delete);

        String tokenString = jwtService.generateToken(username);

        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken(tokenString);
        activationToken.setUser(user);
        activationToken.setExpiryDate(Instant.now().plus(24, ChronoUnit.HOURS)); // data de expiracao e 24 h apos criacao do token
        activationTokenRespository.save(activationToken);

        emailService.sendActivationLink(user.getUsername(), tokenString);

        logger.info("Email para autenticação de usuário enviado para: {} ", user.getUsername());
    }

    @Transactional
    public void activateAccountAndSetPassword(String token, String newPassword) {
        ActivationToken activationToken = activationTokenRespository.findByToken(token)
                .orElseThrow(() -> new InvalidActivationTokenException("Token de ativação inválido ou não encontrado."));

        if (activationToken.getExpiryDate().isBefore(Instant.now())) {
            activationTokenRespository.delete(activationToken);
            throw new InvalidActivationTokenException("Token de ativação expirou.");
        }

        AppUser user = activationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEnabled(true);
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
        appUserRepository.save(user);

        activationTokenRespository.delete(activationToken);

        logger.info("Conta ativada e senha definida para usuário: {}", user.getUsername());
    }

    @Transactional
    public void initiatePasswordReset(String username) {
        AppUser user = appUserRepository.findByUsername(username).orElse(null);

        if (user == null) {
            logger.info("Se uma conta associada a este e-mail for encontrada, um link de redefinição de senha foi enviado.");

            return;
        }

        List<PasswordResetToken> resetTokenList = passwordResetTokenRepository.findByUser(user);
        if (!resetTokenList.isEmpty()) {
            for (PasswordResetToken t : resetTokenList) {
                t.setUsed(true);
            }
            passwordResetTokenRepository.saveAll(resetTokenList);
        }

        String resetTokenString = jwtService.generateToken(username);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetTokenString);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpirationDate(Instant.now().plus(1, ChronoUnit.HOURS));
        passwordResetToken.setIssuedAt(Instant.now());
        passwordResetToken.setUsed(false);
        passwordResetTokenRepository.save(passwordResetToken);

        emailService.sendPasswordResetLink(user.getUsername(), resetTokenString);

        logger.info("Email de redefinição de senha enviado (caso usuário exista) para: {} ", user.getUsername());
    }

    @Transactional
    public void completePasswordReset(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(token).orElseThrow(() -> new InvalidPasswordResetTokenException("Token de redefinição de senha inválido ou não encontrado."));

        if (passwordResetToken.getExpirationDate().isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new InvalidPasswordResetTokenException("Token de redefinição de senha expirou.");
        }
        if (passwordResetToken.getUsed()) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new InvalidPasswordResetTokenException("Token de redefinião de senha já utilizado.");
        }

        AppUser user = passwordResetToken.getUser();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("A nova senha não pode ser igual à senha anterior.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEnabled(true);
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
        appUserRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);

        logger.info("Senha do usuário [{}] redefinida com sucesso", user.getUsername());
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        // Busca usuário por meio de seu Id
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException(userId));

        // Verifica se senha atual recebida corresponde com senha armazenada no banco
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Senha atual incorreta.");
        }

        // Verifica se nova senha não é igual a senha atual.
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("A nova senha não pode ser igual à senha anterior.");
        }

        // Modifica e salva nova senha
        user.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);

        logger.info("Senha do usuário com ID {} alterada com sucesso.", userId);
    }

    @Transactional
    public void logout(String token) {
        jwtService.invalidateToken(token);
        logger.info("Token JWT foi adicionado à blacklist.");
    }

}
