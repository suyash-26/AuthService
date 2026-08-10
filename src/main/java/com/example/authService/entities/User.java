package com.example.authService.entities;

import com.example.authService.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    // Platform-wide role from the v2 hierarchy (SUPER_ADMIN / CENTER_ADMIN / USER).
    // Stored as STRING so the enum can gain values without shifting existing rows.
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isActive = true;

    // Inverse side: FK lives on Address (user_id) since a user can have more than one.
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public  String getName(){
        return Stream.of(firstName, middleName, lastName)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }
}
