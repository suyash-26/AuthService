package com.example.authService.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String addressLine;
    private String state;
    private String district;
    private String country;
    private Integer pincode;
}
