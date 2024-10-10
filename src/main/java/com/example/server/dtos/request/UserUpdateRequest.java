package com.example.server.dtos.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String name;

    String accountType;
    String phone;
    String email;

    String googleAccountId;
    String facebookAccountId;
    boolean isEnabled;

    String zalo;
    String otp;
}
