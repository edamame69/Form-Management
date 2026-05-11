package com.topcv.form.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resource, Long id) {
        return new ResourceNotFoundException(
                String.format("%s with id %d not found", resource, id)
        );
    }
}