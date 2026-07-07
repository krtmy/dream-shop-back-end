package com.helloworld.dreamshopsbackend.exception.category;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
