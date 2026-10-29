package com.lab.insurance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request body for user registration.
 */
public record RegisterRequest(

        @NotBlank(message = "Full name is required")
        @Size(min = 3, max = 100, message = "Full name must be 3-100 characters")
        String fullName,

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 50, message = "Username must be 4-50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "Username may contain letters, digits, '_' and '.' only")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, max = 50, message = "Password must be at least 6 characters")
        String password
) {
}
