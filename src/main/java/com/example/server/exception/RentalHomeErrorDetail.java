package com.example.server.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentalHomeErrorDetail {
    String field;
    String message;
}
