package com.busticket.app.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message){
        super(message);
    }
}
