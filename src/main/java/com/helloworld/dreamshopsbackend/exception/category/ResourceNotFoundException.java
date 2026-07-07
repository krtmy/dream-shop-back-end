package com.helloworld.dreamshopsbackend.exception.category;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
