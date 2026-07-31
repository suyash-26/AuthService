package com.example.authService.contrillers;

import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> addUser(SignupRequest user){
        return ResponseEntity.ok(userService.addUser(user));
    }
    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getALLUsers());
    }
}
