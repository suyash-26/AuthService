package com.example.authService.mockData;

import com.example.authService.entities.User;
import com.example.authService.enums.Role;
import com.example.authService.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Guard against a persistent DB (e.g. the Neon prod database): without this,
        // every restart re-inserts the same 10 emails and crashes on the unique
        // constraint. Fine to skip entirely once the table already has data.
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.saveAll(List.of(

                User.builder()
                        .firstName("John")
                        .middleName("A")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("Jane")
                        .middleName("B")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("Michael")
                        .middleName("C")
                        .lastName("Johnson")
                        .email("michael.johnson@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.SUPER_ADMIN)
                        .build(),

                User.builder()
                        .firstName("Emily")
                        .middleName("D")
                        .lastName("Williams")
                        .email("emily.williams@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("David")
                        .middleName("E")
                        .lastName("Brown")
                        .email("david.brown@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("Sophia")
                        .middleName("F")
                        .lastName("Jones")
                        .email("sophia.jones@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("Daniel")
                        .middleName("G")
                        .lastName("Garcia")
                        .email("daniel.garcia@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("Olivia")
                        .middleName("H")
                        .lastName("Miller")
                        .email("olivia.miller@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build(),

                User.builder()
                        .firstName("James")
                        .middleName("I")
                        .lastName("Davis")
                        .email("james.davis@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.SUPER_ADMIN)
                        .build(),

                User.builder()
                        .firstName("Emma")
                        .middleName("J")
                        .lastName("Wilson")
                        .email("emma.wilson@example.com")
                        .password(passwordEncoder.encode("Password@123"))
                        .role(Role.USER)
                        .build()
        ));
    }
}
