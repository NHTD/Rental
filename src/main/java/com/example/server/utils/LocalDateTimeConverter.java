package com.example.server.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return Timestamp.valueOf(attribute);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        if (dbData == null || dbData.toString().equals("0000-00-00 00:00:00")) {
            return null;
        }
        return dbData.toLocalDateTime();
    }
}