package com.lab.insurance.dto;

/**
 * Simple {"message": "..."} response for actions like purchase / approve.
 */
public record MessageResponse(String message) {
}
