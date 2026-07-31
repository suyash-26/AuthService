package com.example.authService.security;

import com.example.authService.entities.User;
import com.example.authService.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new CustomUserDetails(user);
    }

    // --- Future reference ---
    //
    // If you ever need to load a user by something other than email (e.g. by id when
    // reading the "userId" custom claim from a JWT instead of the "sub" claim), add an
    // overload here, e.g.:
    //
    //     public UserDetails loadUserById(Long id) {
    //         User user = userRepository.findById(id)
    //                 .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    //         return new CustomUserDetails(user);
    //     }
}
