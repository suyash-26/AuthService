package com.example.authService.services;

import com.example.authService.dtos.AddressResponse;
import com.example.authService.dtos.LoginRequest;
import com.example.authService.dtos.LoginResponse;
import com.example.authService.dtos.SignupRequest;
import com.example.authService.dtos.UserResponse;
import com.example.authService.entities.Address;
import com.example.authService.entities.User;
import com.example.authService.enums.Role;
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
        Map<String, Object> toknData = new HashMap<>();
        toknData.put("id",user.getId());
        toknData.put("name",user.getName());
        String token = jwtService.generateToken(user.getEmail(),toknData);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());
        return loginResponse;
    }
    // Auth contract: self-registration always creates a plain USER — CENTER_ADMIN and
    // SUPER_ADMIN are granted out-of-band (center approval / platform ownership), never
    // chosen by the registrant.
    public UserResponse registerUser(SignupRequest signupRequest){
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setMiddleName(signupRequest.getMiddleName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    // Re-fetches by id rather than reusing the User off the security principal: that
    // instance was loaded during JWT authentication, whose Hibernate session is already
    // closed by the time the controller runs, so its lazy `addresses` collection would
    // throw LazyInitializationException. Fetching here keeps it inside the request's
    // open-in-view session.
    public UserResponse getProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .addresses(toAddressResponses(user.getAddresses()))
                .build();
    }

    private List<AddressResponse> toAddressResponses(List<Address> addresses) {
        if (addresses == null) {
            return List.of();
        }
        return addresses.stream()
                .map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .addressLine(address.getAddressLine())
                        .state(address.getState())
                        .district(address.getDistrict())
                        .country(address.getCountry())
                        .pincode(address.getPincode())
                        .build())
                .toList();
    }
}
