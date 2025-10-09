package com.se.skill4hire.service.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(String id) {
        super("Application not found: " + id);
    }

    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
