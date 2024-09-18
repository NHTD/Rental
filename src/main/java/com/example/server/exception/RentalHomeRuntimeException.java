package com.example.server.exception;

public class RentalHomeRuntimeException extends RentalHomeException{
    public RentalHomeRuntimeException(String message) {
        super(message);
    }

    public RentalHomeRuntimeException(String message, Object... arguments) {
        super(message, arguments);
    }

    public RentalHomeRuntimeException(String message, Throwable cause, Object... arguments) {
        super(message, cause, arguments);
    }
}
