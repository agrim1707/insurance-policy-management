package com.lab.insurance.dto;

import jakarta.validation.constraints.Size;

/**
 * Optional remarks the admin adds while approving / rejecting a claim.
 */
public record ClaimDecisionRequest(
        @Size(max = 500, message = "Remarks must be at most 500 characters")
        String remarks
) {
}
