package com.example.LeitorDePlacas.repository;

import com.example.LeitorDePlacas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

