package com.foxminded.services.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(Throwable cause) {
        super (cause);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super (message, cause);
    }

}
