package com.example.server.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest extends SocialAccountRequest{
    @JsonProperty("account_type")
    String accountType;
    String password;
    @JsonProperty("email")
    private String email;
    @JsonProperty("profile_image")
    private String profileImage;
}
