package com.george.springservermanager.exception;

public class ServerNotFoundException extends RuntimeException {

    public ServerNotFoundException(String message) {
        super(message);
    }
}
