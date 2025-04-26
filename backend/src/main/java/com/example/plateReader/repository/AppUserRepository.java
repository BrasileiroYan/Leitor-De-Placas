package com.example.plateReader.repository;

import com.example.plateReader.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

}

