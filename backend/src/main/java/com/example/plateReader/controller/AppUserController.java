package com.example.plateReader.controller;

import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService){
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUserResponseDTO> createUser(@Valid @RequestBody AppUserRequestDTO request) {
        return ResponseEntity.ok().body(appUserService.createUser(request.getUsername(), request.getPassword(), request.getRole()));
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

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<AppUserResponseDTO> findByUsername(@PathVariable String username){
        AppUserResponseDTO appUserDTO = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserResponseDTO> updateById(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO request) {
        return ResponseEntity.ok().body(appUserService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        appUserService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
