package com.example.LeitorDePlacas.repository;

import com.example.LeitorDePlacas.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

}

