package com.kofta.errors;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(Class<?> resource, Integer resourceId) {
        super(
            resource.getSimpleName() +
                " with ID: " +
                resourceId +
                " is not found"
        );
    }
}
