package com.example.server.exception;

public class RentalHomeForbiddenException extends RentalHomeRuntimeException{
    public RentalHomeForbiddenException(String message) {
        super(message);
    }

    public RentalHomeForbiddenException(String message, Object... arguments) {
        super(message, arguments);
    }

    public RentalHomeForbiddenException(String message, Throwable cause, Object... arguments) {
        super(message, cause, arguments);
    }
}
