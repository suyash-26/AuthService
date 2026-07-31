package com.example.authService.contrillers;

import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.security.CustomUserDetails;
import com.example.authService.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> addUser(@RequestBody SignupRequest user){
        return ResponseEntity.ok(userService.addUser(user));
    }
    @GetMapping("/")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }
    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                     @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUser(currentUser.getUser().getId(), userRequest));
    }
}
