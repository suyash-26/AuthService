package com.example.authService.services;

import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.entities.User;
import com.example.authService.repositories.UserRepositories;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    UserRepositories userRepositories;
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
        user.setPassword(signupRequest.getPassword());
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
    public List<UserResponse> getALLUsers(){
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
}
