package com.example.authService.contrillers;

import com.example.authService.dtos.AddressRequest;
import com.example.authService.dtos.AddressResponse;
import com.example.authService.security.CustomUserDetails;
import com.example.authService.services.AddressService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
@AllArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                        @RequestBody AddressRequest request) {
        AddressResponse response = addressService.addAddress(currentUser.getUser().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(addressService.getAddressesForUser(currentUser.getUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                        @PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddress(currentUser.getUser().getId(), id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                           @PathVariable Long id,
                                                           @RequestBody AddressRequest request) {
        return ResponseEntity.ok(addressService.updateAddress(currentUser.getUser().getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal CustomUserDetails currentUser,
                                               @PathVariable Long id) {
        addressService.deleteAddress(currentUser.getUser().getId(), id);
        return ResponseEntity.noContent().build();
    }
}
