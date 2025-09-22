package com.se.skill4hire.service.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(Long id) {
        super("Application not found: " + id);
    }

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
