package com.example.authService.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// Deliberately has no `user` field, unlike the Address entity — including it here would
// serialize User -> addresses -> this same Address -> User -> ... in an infinite loop.
@Getter
@Setter
@Builder
public class AddressResponse {
    private Long id;
    private String addressLine;
    private String state;
    private String district;
    private String country;
    private Integer pincode;
}
