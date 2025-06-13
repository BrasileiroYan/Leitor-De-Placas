package com.example.plateReader.controller;

import com.example.plateReader.dto.*;
import com.example.plateReader.service.AuthService;
import com.example.plateReader.service.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        AuthResponseDTO response = authService.authenticateUser(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activate-account")
    public ResponseEntity<String> setPassword(@Valid @RequestBody SetPasswordRequestDTO request) {
        authService.activateAccountAndSetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Senha definida com sucesso. Você já pode fazer o login");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> requestPasswordReset(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        authService.initiatePasswordReset(request.getUsername());
        return ResponseEntity.ok("Se uma conta associada a este e-mail for encontrada, um link de redefinição de senha foi enviado.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> completePasswordReset(@Valid @RequestBody ResetPasswordRequestDTO request) {
        authService.completePasswordReset(request.getResetToken(), request.getNewPassword());
        return ResponseEntity.ok("Senha redefinida com sucesso. Você já pode fazer o login.");
    }
}
