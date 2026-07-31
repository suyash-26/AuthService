package com.example.authService.repositories;

import com.example.authService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositories extends JpaRepository<User,Long>{
    public User findByFirstName(String firstName);
}
