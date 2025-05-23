package com.example.plateReader.service;

import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.repository.AppUserRepository;
import com.example.plateReader.service.exception.AppUserNotFoundException;
import com.example.plateReader.service.exception.UsernameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder encoder){
        this.appUserRepository = appUserRepository;
        this.encoder = encoder;
    }

    public AppUserResponseDTO createUser(AppUserRequestDTO request) {

        if (appUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        appUserRepository.save(user);

        return new AppUserResponseDTO(user);
    }

    public List<AppUserResponseDTO> findAll(){
        return appUserRepository.findAll()
                .stream().map(AppUserResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AppUserResponseDTO findById(Long id){
        AppUser appUser = appUserRepository.findById(id).orElseThrow(() -> new AppUserNotFoundException(id));

        return new AppUserResponseDTO(appUser);
    }

    public AppUserResponseDTO findByUsername(String username){
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(() -> new AppUserNotFoundException(username));

        return new AppUserResponseDTO(appUser);
    }

    public AppUserResponseDTO updateById(Long id, AppUserRequestDTO request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new AppUserNotFoundException(id));

        if(request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if(request.getPassword() != null &&
                !request.getPassword().isBlank() &&
                !encoder.matches(request.getPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(request.getPassword()));
        }

        user.setRole(request.getRole());

        appUserRepository.save(user);
        return new AppUserResponseDTO(user);
    }

    public void deleteById(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new AppUserNotFoundException(id);
        }

        appUserRepository.deleteById(id);
    }
}
