package com.example.plateReader.controller.authentication;

import com.example.plateReader.controller.exception.StandardError;
import com.example.plateReader.dto.*;
import com.example.plateReader.dto.authentication.*;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.tokens.RefreshToken;
import com.example.plateReader.service.authentication.AuthService;
import com.example.plateReader.service.authentication.JwtService;
import com.example.plateReader.service.authentication.RefreshTokenService;
import com.example.plateReader.service.exception.authentication.RefreshTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para login, refresh e gerenciamento de sessão")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Autentica um usuário", description = "Realiza o login com usuário e senha para obter um access token e um refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO Request) {
        AuthResponseDTO response = authService.authenticateUser(Request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Altera a senha e ativa a conta", description = "Usa um token temporário para fazer o usuário definir a senha e ativa sua conta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta ativada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Token de ativação inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/activate-account")
    public ResponseEntity<MessageResponseDTO> setPassword(@Valid @RequestBody SetPasswordRequestDTO request) {
        authService.activateAccountAndSetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO("Senha definida com sucesso. Você já pode fazer o login"));
    }

    @Operation(summary = "Solicita alteração de senha esquecida", description = "Gera um token de redefinição de senha e envia o e-mail para direcionar o usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "E-mail enviado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Erro ao enviar e-mail/gerar token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> requestPasswordReset(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        authService.initiatePasswordReset(request.getUsername());
        return ResponseEntity.ok(new MessageResponseDTO("Se uma conta associada a este e-mail for encontrada, um link de redefinição de senha foi enviado."));
    }

    @Operation(summary = "Realiza a alteração da senha esquecida", description = "Valida o token de redefinição e troca a senha do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alteração de senha bem-sucedida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Token inválido ou senhas iguais",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> completePasswordReset(@Valid @RequestBody ResetPasswordRequestDTO request) {
        authService.completePasswordReset(request.getResetToken(), request.getNewPassword());
        return ResponseEntity.ok(new MessageResponseDTO("Senha redefinida com sucesso. Você já pode fazer o login."));
    }

    @Operation(summary = "Muda a senha atual do usuário", description = "Realiza a mudança da senha atual para uma nova se o usuário conhecer a antiga.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha trocada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou senhas iguais",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDTO> changePassword(@AuthenticationPrincipal AppUser loggedInUser, @Valid @RequestBody ChangePasswordRequestDTO request) {

        Long userId = loggedInUser.getId();

        authService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok(new MessageResponseDTO("Senha alterada com sucesso. Você já pode fazer o login"));
    }

    @Operation(summary = "Atualiza o token de acesso", description = "Usa um refresh token válido para gerar um novo access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token inválido ou expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
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

    @Operation(summary = "Desloga um usuário", description = "Realiza o logout do usuário e invalida seu token de acesso, adicionando-o a uma blacklist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.noContent().build();
    }
}
