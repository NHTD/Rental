package com.example.server.dtos.response;

import com.example.server.models.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;

    String name;

    String accountType;

    String password;
    String email;

    String zalo;
    String googleAccountId;
    String facebookAccountId;

    Set<Role> roles;
}