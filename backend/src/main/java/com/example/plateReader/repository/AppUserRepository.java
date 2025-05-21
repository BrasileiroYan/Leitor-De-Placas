package com.example.plateReader.repository;

import com.example.plateReader.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Override
    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByUsername(String username);
}

