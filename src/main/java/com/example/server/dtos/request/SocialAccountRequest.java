package com.example.server.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class SocialAccountRequest {
    @JsonProperty("facebook_account_id")
    protected String facebookAccountId;

    @JsonProperty("google_account_id")
    protected String googleAccountId;

    public boolean isFacebookAccountIdValid() {
        return facebookAccountId != null && !facebookAccountId.isEmpty();
    }

    public boolean isGoogleAccountIdValid() {
        return googleAccountId != null && !googleAccountId.isEmpty();
    }

    // Phương thức kiểm tra xem người dùng có phải là người dùng đăng nhập xã hội hay không
    public boolean isSocialLogin() {
        return (facebookAccountId != null && !facebookAccountId.isEmpty()) ||
                (googleAccountId != null && !googleAccountId.isEmpty());
    }
}
