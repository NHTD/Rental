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
public class UserCreationRequest {
    String name;

    String accountType;

    String password;
    String googleAccountId;
    String facebookAccountId;
    boolean isEnabled;

    String zalo;

    Set<String> roles;
}
