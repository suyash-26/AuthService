package com.example.authService.dtos;

import com.example.authService.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Deliberately lighter than UserResponse — no addresses. Listing/searching many users
// and eagerly walking each one's addresses collection would be an N+1 lazy-load per
// row; this is what an admin actually needs to identify who to grant access to.
@Getter
@Setter
@Builder
public class UserSummaryResponse {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Role role;
}
