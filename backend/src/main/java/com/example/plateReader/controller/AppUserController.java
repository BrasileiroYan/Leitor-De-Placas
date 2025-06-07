package com.example.plateReader.controller;

import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    public AppUserController(AppUserService appUserService, PasswordEncoder passwordEncoder){
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<AppUserResponseDTO> createUser(@Valid @RequestBody AppUserRequestDTO request) {

        return ResponseEntity.ok().body(appUserService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponseDTO>> findAll(){
        List<AppUserResponseDTO> appUserDTO = appUserService.findAll();

        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<AppUserResponseDTO> findById(@PathVariable Long id){
        AppUserResponseDTO appUserDTO = appUserService.findById(id);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/email/{email}")
    public ResponseEntity<AppUserResponseDTO> findByUsername(@PathVariable String username){
        AppUserResponseDTO appUserDTO = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserResponseDTO> updateById(@PathVariable Long id, @RequestBody AppUserRequestDTO Request) {
        return ResponseEntity.ok().body(appUserService.updateById(id, Request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        appUserService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
