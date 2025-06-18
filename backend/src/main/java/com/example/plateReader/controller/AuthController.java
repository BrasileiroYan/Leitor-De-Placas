package com.example.plateReader.controller;

import com.example.plateReader.dto.*;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.tokens.RefreshToken;
import com.example.plateReader.service.AuthService;
import com.example.plateReader.service.JwtService;
import com.example.plateReader.service.RefreshTokenService;
import com.example.plateReader.service.exception.RefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
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

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String requestRefreshToken = request.refreshToken();

        Optional<RefreshToken> refreshTokenOpt = refreshTokenService.findByToken(requestRefreshToken);

        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenOpt.get());
            AppUser userFromToken = refreshToken.getUser();
            String newAccessToken = jwtService.generateToken(userFromToken.getUsername());
            return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, requestRefreshToken));

        } else {
            throw new RefreshTokenException(requestRefreshToken, "Refresh token não encontrado no banco de dados.");
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token Bearer ausente ou mal formatado.");
        }
        String token = authHeader.substring(7);

        authService.logout(token);

        return ResponseEntity.ok("Logout realizado com sucesso.");
    }

}
