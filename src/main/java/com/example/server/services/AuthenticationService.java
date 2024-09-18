package com.example.server.services;

import com.example.server.dtos.request.AuthenticationRequest;
import com.example.server.dtos.response.AuthenticationResponse;

import java.io.IOException;
import java.util.Map;

public interface AuthenticationService {
    AuthenticationResponse signIn(AuthenticationRequest request);
    String generateUrl(String loginType);
    Map<String, Object> authenticateAndFetchProfile(String code, String loginType) throws IOException;
}
