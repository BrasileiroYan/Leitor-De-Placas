package com.example.plateReader.service;

import com.example.plateReader.dto.AuthRequestDTO;
import com.example.plateReader.dto.AuthResponseDTO;
import com.example.plateReader.model.ActivationToken;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import com.example.plateReader.repository.ActivationTokenRespository;
import com.example.plateReader.repository.AppUserRepository;
import com.example.plateReader.service.exception.AppUserNotFoundException;
import com.example.plateReader.service.exception.InvalidActivationTokenException;
import com.example.plateReader.service.exception.UsernameAlreadyExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.plateReader.service.exception.InvalidCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final ActivationTokenRespository activationTokenRespository;
    private final AppUserRepository appUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(ActivationTokenRespository activationTokenRespository, AppUserRepository appUserRepository, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.activationTokenRespository = activationTokenRespository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

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
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(() -> new AppUserNotFoundException(username));

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


        emailService.sendActivationLink(
                username,
                "[PRF] Ativação de conta – Acesse o link para seu primeiro acesso",
                tokenString
        );
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
    }
}
