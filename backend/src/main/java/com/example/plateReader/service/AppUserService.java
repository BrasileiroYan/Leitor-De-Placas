package com.example.plateReader.service;

import com.example.plateReader.Utils.PasswordGenerator;
import com.example.plateReader.dto.AppUserRequestDTO;
import com.example.plateReader.dto.AppUserResponseDTO;
import com.example.plateReader.model.AppUser;
import com.example.plateReader.model.enums.Role;
import com.example.plateReader.repository.AppUserRepository;
import com.example.plateReader.service.exception.AppUserNotFoundException;
import com.example.plateReader.service.exception.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder encoder){
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = encoder;
    }

    public AppUserResponseDTO createUser(String username, String rawPassword, Role role) {
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setEnabled(false);

        return new AppUserResponseDTO(appUserRepository.save(user));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário com o nome " + username + "não encontrado"));

        String roleName = user.getRole().name();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }

    public AppUserResponseDTO updateById(Long id, AppUserRequestDTO request) {
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new AppUserNotFoundException(id));

        if(request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if(appUserRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new UsernameAlreadyExistsException(request.getUsername());
            }

            user.setUsername(request.getUsername());
        }

        if(request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if(request.getRole() != null) {
            user.setRole(request.getRole());
        }

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
