package br.com.prf.leitordeplacas.service;

import br.com.prf.leitordeplacas.dto.AppUserRequestDTO;
import br.com.prf.leitordeplacas.dto.AppUserResponseDTO;
import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.enums.Role;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import br.com.prf.leitordeplacas.service.exception.AppUserNotFoundException;
import br.com.prf.leitordeplacas.service.exception.UsernameAlreadyExistsException;
import br.com.prf.leitordeplacas.service.exception.authentication.AccountAlreadyUnlockedException;
import br.com.prf.leitordeplacas.service.exception.authentication.AppUserAlreadyDisabledException;
import br.com.prf.leitordeplacas.service.exception.authentication.AppUserAlreadyEnabledException;
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

    public AppUserResponseDTO createUserByAdmin(String username) {
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(null);
        user.setRole(Role.STANDARD);
        user.setEnabled(false);
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return user;
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

    public void enableUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException(userId));

        if (user.isEnabled()) {
           throw new AppUserAlreadyEnabledException("Usuário já está habilitado.");
        }

        user.setEnabled(true);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
        appUserRepository.save(user);
    }

    public void disableUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new AppUserAlreadyDisabledException("Usuário já está desabilitado.");
        }

        user.setEnabled(false);
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
        appUserRepository.save(user);
    }

    public void unlockUser(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException(userId));

        if (!user.isAccountLocked()) {
            throw new AccountAlreadyUnlockedException("Usuário não está bloqueado");
        }

        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        user.setConsecutiveLockouts(0);
        appUserRepository.save(user);
    }
}
