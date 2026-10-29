package com.lab.insurance.exception;

/**
 * Thrown when a requested entity (policy / user policy / claim) does not exist.
 * Handled globally as HTTP 404 by {@link GlobalExceptionHandler}.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super("%s with id %d not found".formatted(resourceName, id));
    }
}
