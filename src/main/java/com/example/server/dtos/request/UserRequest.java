package com.example.server.dtos.request;

import com.example.server.models.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String name;

    String accountType;
    String phone;
    String email;

    String password;
    String googleAccountId;
    String facebookAccountId;
    boolean isEnabled;

    String zalo;
    String otp;
    String avatar;

    Set<String> roles;
}
