package br.com.prf.leitordeplacas.config;

import br.com.prf.leitordeplacas.model.AppUser;
import br.com.prf.leitordeplacas.model.enums.Role;
import br.com.prf.leitordeplacas.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        assert userRepository != null;
        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin@prf.gov.br");
            admin.setPassword(passwordEncoder.encode("@Admin_PRF123")); //
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setFailedLoginAttempts(0);
            admin.setAccountLocked(false);
            admin.setLockTime(null);
            admin.setConsecutiveLockouts(0);
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            AppUser user = new AppUser();
            user.setUsername("user@prf.gov.br");
            user.setPassword(passwordEncoder.encode("@Usuario123"));
            user.setRole(Role.STANDARD);
            user.setEnabled(true);
            user.setFailedLoginAttempts(0);
            user.setAccountLocked(false);
            user.setLockTime(null);
            user.setConsecutiveLockouts(0);
            userRepository.save(user);
        }
    }
}
