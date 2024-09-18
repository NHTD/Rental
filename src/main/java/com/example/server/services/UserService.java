package com.example.server.services;

import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.UserResponse;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface UserService {
    UserResponse createUser(UserCreationRequest request) throws MessagingException, IOException;
    UserResponse getUser(String id);
}
