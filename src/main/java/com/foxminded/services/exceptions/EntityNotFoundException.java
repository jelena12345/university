package com.foxminded.services.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Throwable cause) {
        super (cause);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super (message, cause);
    }

}
