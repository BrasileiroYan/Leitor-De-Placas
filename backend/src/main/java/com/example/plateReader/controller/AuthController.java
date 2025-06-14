package com.example.plateReader.controller;

import com.example.plateReader.dto.*;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO Request) {
        AuthResponseDTO response = authService.authenticateUser(Request);
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

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal AppUser loggedInUser, @Valid @RequestBody ChangePasswordRequestDTO request) {

        Long userId = loggedInUser.getId();

        authService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok("Senha alterada com sucesso. Você já pode fazer o login");
    }
}
