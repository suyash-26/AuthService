package com.example.authService.contrillers;

import com.example.authService.dtos.LoginRequest;
import com.example.authService.dtos.LoginResponse;
import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UpdateRoleRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.dtos.UserSummaryResponse;
import com.example.authService.security.CustomUserDetails;
import com.example.authService.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody SignupRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails currentUser){
        return ResponseEntity.ok(userService.getProfile(currentUser.getUser().getId()));
    }

    @PostMapping("/users/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> updateRoleOfUser(@PathVariable("id") Long userId,
                                                          @Valid @RequestBody UpdateRoleRequest request){
        return ResponseEntity.ok(userService.updateUserRole(userId, request.getRole()));
    }

    // Browse/search users before granting access. One optional free-text term matched
    // against first/last name or email — omit it to list everyone.
    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<UserSummaryResponse>> getAllUsers(
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(userService.getAllUsers(search));
    }

    // Full detail (including addresses) for one user by id — same lookup /auth/me
    // uses for "myself", exposed here for an admin looking up anyone by id.
    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> getUserDetails(@PathVariable("id") Long userId){
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
