package com.example.authService.repositories;

import com.example.authService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    public User findByFirstName(String firstName);
    Optional<User> findByEmail(String email);

    // Admin search: one free-text term matched against first/last name or email, so a
    // single search box covers "by name or email" without needing separate endpoints
    // for each field.
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName, String lastName, String email);
}
