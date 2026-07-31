package com.example.authService.services;

import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.entities.User;
import com.example.authService.repositories.UserRepository;
import com.example.authService.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    final private UserRepository userRepositories;
    final private PasswordEncoder passwordEncoder;
    final private JwtService jwtService;
    public User getUserByName(String firstName){
        User user = userRepositories.findByFirstName(firstName);
        return user;
    }
    public UserResponse addUser(SignupRequest signupRequest){
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setMiddleName(signupRequest.getMiddleName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        User savedUser = userRepositories.save(user);
        UserResponse userResponse = UserResponse.builder()
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .middleName(savedUser.getMiddleName())
                .lastName(savedUser.getLastName())
                .id(savedUser.getId())
                .build();
        return userResponse;
    }
    public List<UserResponse> getAllUsers(){
        List<User> users = userRepositories.findAll();
        List<UserResponse> allUsers = users.stream().map(user -> {
            return UserResponse.builder()
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .middleName(user.getMiddleName())
                    .lastName(user.getLastName())
                    .id(user.getId())
                    .build();
        }).toList();
        return allUsers;
    }
    public UserResponse findUserById(Long id){
        User user = userRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
    public UserResponse updateUser(Long userId, UserRequest userRequest){
        User user = userRepositories.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRequest.getFirstName() != null) {
            user.setFirstName(userRequest.getFirstName());
        }
        if (userRequest.getMiddleName() != null) {
            user.setMiddleName(userRequest.getMiddleName());
        }
        if (userRequest.getLastName() != null) {
            user.setLastName(userRequest.getLastName());
        }
        if (userRequest.getEmail() != null) {
            user.setEmail(userRequest.getEmail());
        }

        User savedUser = userRepositories.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .middleName(savedUser.getMiddleName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
    }
}
