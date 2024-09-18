package com.example.server.exception;

public class RentalHomeDataModelNotFoundException extends RentalHomeRuntimeException{
    public RentalHomeDataModelNotFoundException(String message) {
        super(message);
    }

    public RentalHomeDataModelNotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }

    public RentalHomeDataModelNotFoundException(String message, Throwable cause, Object... arguments) {
        super(message, cause, arguments);
    }
}
