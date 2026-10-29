package com.lab.insurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Request body for submitting a claim on a purchased policy. At least one
 * supporting document is mandatory.
 */
public record ClaimRequest(

        @NotNull(message = "purchasedPolicyId is required - which of your policies is the claim on?")
        Long purchasedPolicyId,

        @NotNull(message = "Claim amount is required")
        @DecimalMin(value = "1.0", message = "Claim amount must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Claim amount looks invalid")
        BigDecimal claimAmount,

        @NotBlank(message = "Incident description is required")
        @Size(min = 10, max = 1000, message = "Describe the incident in 10-1000 characters")
        String incidentDescription,

        @NotEmpty(message = "At least one supporting document is required")
        List<@NotBlank(message = "Document name must not be blank") String> documents
) {
}
