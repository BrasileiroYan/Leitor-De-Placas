package com.example.plateReader.controller;

import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.service.AppUserService;
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
    public AppUserResponseDTO create(@RequestBody AppUserRequestDTO request) {
        return appUserService.createUser(request);
    }

    @PutMapping("/{id}")
    public AppUserResponseDTO update(@PathVariable Long id, @RequestBody AppUserRequestDTO Request) {
        return appUserService.updateUser(id, Request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        appUserService.deleteUser(id);
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponseDTO>> findAll(){
        List<AppUserResponseDTO> appUserDTO = appUserService.findAll();

        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<AppUserResponseDTO> findByID(@PathVariable Long id){
        AppUserResponseDTO appUserDTO = appUserService.findById(id);
        return ResponseEntity.ok().body(appUserDTO);
    }

    @GetMapping(path = "/username/{username}")
    public ResponseEntity<AppUserResponseDTO> findByUsername(@PathVariable String username){
        AppUserResponseDTO appUserDTO = appUserService.findByUsername(username);
        return ResponseEntity.ok().body(appUserDTO);
    }
}
