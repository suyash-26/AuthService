package com.example.authService.repositories;

import com.example.authService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    public User findByFirstName(String firstName);
    Optional<User> findByEmail(String email);
}
