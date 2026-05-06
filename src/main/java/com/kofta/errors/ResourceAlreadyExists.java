package com.kofta.errors;

public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists(Class<?> resource) {
        super(resource.getSimpleName() + " already exists");
    }
}
