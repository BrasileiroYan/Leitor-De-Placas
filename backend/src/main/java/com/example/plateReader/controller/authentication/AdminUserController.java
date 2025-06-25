package com.example.plateReader.controller.authentication;

import com.example.plateReader.controller.exception.StandardError;
import com.example.plateReader.dto.authentication.AdminCreateUserRequestDTO;
import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
import com.example.plateReader.service.authentication.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users")
@Tag(name = "Admin", description = "Endpoints para a gestão de usuários, configurações do sistema e outras funcionalidades exclusivas para administradores.")
public class AdminUserController {

    private final AppUserService appUserService;
    private final AuthService authService;

    public AdminUserController(AppUserService appUserService, AuthService authService){
        this.appUserService = appUserService;
        this.authService = authService;
    }

    @Operation(summary = "Cria e ativa um novo usuário via administrador",
            description = "Permite a um administrador criar um novo usuário e iniciar o processo de ativação da conta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado e processo de ativação iniciado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou usuário já existente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PostMapping(path = "/create-user-and-activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> createAndActiveUser(@Valid @RequestBody AdminCreateUserRequestDTO request) {

        AppUserResponseDTO createdUser = appUserService.createUserByAdmin(request.getUsername());

        authService.initiateUserActivation(request.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @Operation(summary = "Lista todos os usuários do sistema",
            description = "Retorna uma lista completa de todos os usuários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserResponseDTO[].class))), // Array de DTOs
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUserResponseDTO>> findAll(){
        List<AppUserResponseDTO> appUserDTO = appUserService.findAll();

        return ResponseEntity.ok().body(appUserDTO);
    }


    @Operation(summary = "Busca um usuário por ID",
            description = "Retorna os detalhes de um usuário específico, dado o seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> findById(@PathVariable Long id){
        AppUserResponseDTO appUserDTO = appUserService.findById(id);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @Operation(summary = "Busca um usuário por nome de usuário (username)",
            description = "Retorna os detalhes de um usuário específico, dado o seu username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o username especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @GetMapping(path = "/by-username/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> findByUsername(@PathVariable String username){
        AppUserResponseDTO appUserDTO = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(appUserDTO);
    }


    @Operation(summary = "Atualiza os dados de um usuário por ID",
            description = "Permite a um administrador atualizar as informações de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> updateById(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO request) {
        return ResponseEntity.ok().body(appUserService.updateById(id, request));
    }

    @Operation(summary = "Exclui um usuário por ID",
            description = "Remove permanentemente um usuário do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        appUserService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Habilita um usuário por ID",
            description = "Define a conta de um usuário como habilitada, permitindo seu acesso ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário habilitado com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        appUserService.enableUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desabilita um usuário por ID",
            description = "Define a conta de um usuário como desabilitada, impedindo seu acesso ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário desabilitado com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        appUserService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desbloqueia um usuário por ID",
            description = "Remove o bloqueio de uma conta de usuário, caso ela tenha sido bloqueada devido a múltiplas tentativas de login falhas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário desbloqueado com sucesso (sem conteúdo de retorno)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado, o usuário autenticado não possui 'ROLE_ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    })
    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id) {
        appUserService.unlockUser(id);
        return ResponseEntity.noContent().build();
    }
}
