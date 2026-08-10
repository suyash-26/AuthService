package com.example.authService.dtos;

import com.example.authService.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Role role;
    private List<AddressResponse> addresses;

    private UserResponse(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.role = builder.role;
        this.addresses = builder.addresses;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String middleName;
        private String lastName;
        private String email;
        private Role role;
        private List<AddressResponse> addresses;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder addresses(List<AddressResponse> addresses){
            this.addresses = addresses;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}