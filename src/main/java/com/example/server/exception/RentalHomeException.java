package com.example.server.exception;

import com.example.server.utils.FormatUtils;

public abstract class RentalHomeException extends RuntimeException{
    public RentalHomeException(String message) {
        super(message);
    }

    public RentalHomeException(String message, Object ...arguments) {
        super(formatMessage(message, arguments));
    }

    public RentalHomeException(String message, Throwable cause, Object ...arguments) {
        super(formatMessage(message, arguments), cause);
    }

    public static String formatMessage(String message, Object ...arguments) {
        return FormatUtils.format(message, arguments);
    }
}
