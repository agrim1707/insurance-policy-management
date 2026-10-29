package com.lab.insurance.exception;

/**
 * Thrown when an action breaks a business rule (duplicate username, claim
 * amount above coverage, deciding an already-decided claim, etc).
 * Handled globally as HTTP 400 by {@link GlobalExceptionHandler}.
 */
public class InvalidActionException extends RuntimeException {

    public InvalidActionException(String message) {
        super(message);
    }
}
