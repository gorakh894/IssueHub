package com.issuehub.exception;

/**
 * Exception thrown when the request is invalid
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}
