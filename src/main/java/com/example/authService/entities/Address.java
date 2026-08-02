package com.example.authService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    private String state;

    private String district;

    private String country;

    private Integer pincode;

    // Owning side of the relationship: a user can have several addresses (home, work,
    // a pet pickup location, etc.), so the FK lives here rather than a single address_id
    // column on User.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
