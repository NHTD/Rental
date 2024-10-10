package com.example.server.services;

import com.example.server.dtos.request.AuthenticationRequest;
import com.example.server.dtos.request.PasswordRequest;
import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public interface AuthenticationService {
    AuthenticationResponse signIn(AuthenticationRequest request);
    String generateUrl(String loginType);
    Map<String, Object> authenticateAndFetchProfile(String code, String loginType) throws IOException;
    void changePassword(String accountType, PasswordRequest request);
    void verifyAccount(String account) throws MessagingException, IOException;
}
