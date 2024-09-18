package com.example.server.exception;

public class RentalHomeDataInvalidException extends RentalHomeRuntimeException{
    public RentalHomeDataInvalidException(String message) {
        super(message);
    }

    public RentalHomeDataInvalidException(String message, Object... arguments) {
        super(message, arguments);
    }

    public RentalHomeDataInvalidException(String message, Throwable cause, Object... arguments) {
        super(message, cause, arguments);
    }
}
