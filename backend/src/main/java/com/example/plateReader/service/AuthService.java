package com.example.plateReader.service;

import com.example.plateReader.dto.AuthRequestDTO;
import com.example.plateReader.dto.AuthResponseDTO;
import com.example.plateReader.model.tokens.ActivationToken;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.tokens.PasswordResetToken;
import com.example.plateReader.repository.tokens.ActivationTokenRespository;
import com.example.plateReader.repository.AppUserRepository;
import com.example.plateReader.repository.tokens.PasswordResetTokenRepository;
import com.example.plateReader.service.exception.*;
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

    private final ActivationTokenRespository activationTokenRespository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(ActivationTokenRespository activationTokenRespository, PasswordResetTokenRepository passwordResetTokenRepository, AppUserRepository appUserRepository, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.activationTokenRespository = activationTokenRespository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO authenticateUser(AuthRequestDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return new AuthResponseDTO(token);

        } catch (AuthenticationException e) {
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
}
