package com.example.authService.security;

import com.example.authService.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Access to the underlying entity, e.g. via @AuthenticationPrincipal in a controller
    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("role" + user.getRole());
        if (user.getRole() == null) {
            return Collections.emptyList();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    // --- Future reference: other things you may need to wire in here later ---
    //
    // - Account locking: if you add a "failedLoginAttempts" or "lockedUntil" field to User,
    //   return that condition from isAccountNonLocked() instead of hardcoding true.
    //
    // - Password expiry: if you add password-rotation policy, return the real check
    //   from isCredentialsNonExpired() instead of hardcoding true.
    //
    // - Account expiry: if users can have a subscription/trial end date, return that
    //   check from isAccountNonExpired() instead of hardcoding true.
    //
    // - Multiple roles/permissions: if User.role becomes a collection instead of a single
    //   enum, update getAuthorities() to map every role/permission to a GrantedAuthority.
}
