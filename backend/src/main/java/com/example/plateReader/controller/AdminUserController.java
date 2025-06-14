package com.example.plateReader.controller;

import com.example.plateReader.dto.AdminCreateUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
import com.example.plateReader.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
