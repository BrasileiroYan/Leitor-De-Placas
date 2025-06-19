package com.example.plateReader.controller;

import com.example.plateReader.dto.AdminCreateUserRequestDTO;
import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
import com.example.plateReader.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users")
public class AdminUserController {

    private final AppUserService appUserService;
    private final AuthService authService;

    public AdminUserController(AppUserService appUserService, AuthService authService){
        this.appUserService = appUserService;
        this.authService = authService;
    }

    @PostMapping(path = "/create-user-and-activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> createAndActiveUser(@Valid @RequestBody AdminCreateUserRequestDTO request) {

        AppUserResponseDTO createdUser = appUserService.createUserByAdmin(request.getUsername());

        authService.initiateUserActivation(request.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AppUserResponseDTO>> findAll(){
        List<AppUserResponseDTO> appUserDTO = appUserService.findAll();

        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> findById(@PathVariable Long id){
        AppUserResponseDTO appUserDTO = appUserService.findById(id);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/by-username/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> findByUsername(@PathVariable String username){
        AppUserResponseDTO appUserDTO = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AppUserResponseDTO> updateById(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO request) {
        return ResponseEntity.ok().body(appUserService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        appUserService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        appUserService.enableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable Long id) {
        appUserService.disableUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id) {
        appUserService.unlockUser(id);
        return ResponseEntity.noContent().build();
    }
}
