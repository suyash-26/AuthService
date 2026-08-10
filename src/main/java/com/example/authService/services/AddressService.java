package com.example.authService.services;

import com.example.authService.dtos.AddressRequest;
import com.example.authService.dtos.AddressResponse;
import com.example.authService.entities.Address;
import com.example.authService.entities.User;
import com.example.authService.repositories.AddressRepository;
import com.example.authService.repositories.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressResponse addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Address address = new Address();
        address.setUser(user);
        applyRequest(address, request);

        return toResponse(addressRepository.save(address));
    }

    public List<AddressResponse> getAddressesForUser(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AddressResponse getAddress(Long userId, Long addressId) {
        return toResponse(findOwnedOrThrow(userId, addressId));
    }

    public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {
        Address address = findOwnedOrThrow(userId, addressId);
        applyRequest(address, request);
        return toResponse(addressRepository.save(address));
    }

    public void deleteAddress(Long userId, Long addressId) {
        Address address = findOwnedOrThrow(userId, addressId);
        addressRepository.delete(address);
    }

    // Scoping the lookup to (addressId, userId) together — rather than fetching by id
    // and checking ownership after — means a wrong/other user's id 404s exactly like a
    // nonexistent one, instead of leaking via a 403 that the id itself is valid.
    private Address findOwnedOrThrow(Long userId, Long addressId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
    }

    private void applyRequest(Address address, AddressRequest request) {
        if (request.getAddressLine() != null) {
            address.setAddressLine(request.getAddressLine());
        }
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        if (request.getDistrict() != null) {
            address.setDistrict(request.getDistrict());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        if (request.getPincode() != null) {
            address.setPincode(request.getPincode());
        }
    }

    private AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .state(address.getState())
                .district(address.getDistrict())
                .country(address.getCountry())
                .pincode(address.getPincode())
                .build();
    }
}
