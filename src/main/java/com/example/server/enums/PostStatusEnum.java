package com.example.server.enums;

import com.example.server.exception.RentalHomeDataModelNotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum PostStatusEnum {
    EXPIRED,
    ACTIVE;

    public static PostStatusEnum fromString(String stringStatus){
        return Arrays.stream(PostStatusEnum.values())
                .filter(s -> StringUtils.equalsIgnoreCase(s.name(), stringStatus))
                .findFirst()
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Unsupported unit status type: {}", stringStatus));
    }
}
