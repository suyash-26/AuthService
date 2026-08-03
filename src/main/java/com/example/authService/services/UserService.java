package com.example.authService.services;

import com.example.authService.dtos.LoginRequest;
import com.example.authService.dtos.LoginResponse;
import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.entities.User;
import com.example.authService.repositories.UserRepository;
import com.example.authService.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtService jwtService;
    public User getUserByName(String firstName){
        User user = userRepository.findByFirstName(firstName);
        return user;
    }
    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        Map<String, Object> toeknData = new HashMap<>();
        toeknData.put("id",user.getId());
        toeknData.put("name",user.getName());
        String token = jwtService.generateToken(user.getEmail(),toeknData);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());
        return loginResponse;
    }
    public UserResponse addUser(SignupRequest signupRequest){
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setMiddleName(signupRequest.getMiddleName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        User savedUser = userRepository.save(user);
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
        List<User> users = userRepository.findAll();
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
        User user = userRepository.findById(id)
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
        User user = userRepository.findById(userId)
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

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .middleName(savedUser.getMiddleName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
    }
}
