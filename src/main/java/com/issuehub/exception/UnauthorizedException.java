package com.issuehub.exception;

/**
 * Exception thrown when authentication fails or access is denied
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}
