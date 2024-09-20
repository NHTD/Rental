package com.example.server.enums;

import com.example.server.exception.RentalHomeDataModelNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum UserStatusEnum {
    VALID,
    INVALID;

    public static UserStatusEnum fromString(String status) {
        return Arrays.stream(UserStatusEnum.values())
                .filter(s -> StringUtils.equalsIgnoreCase(s.name(), status))
                .findFirst()
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Unsupported unit status type: {}", status));
    }
}
