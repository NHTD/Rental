package com.example.server.services;

import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface UserService {
    UserResponse createUser(UserCreationRequest request, UserStatusEnum status) throws MessagingException, IOException;
    UserResponse getUser(String id);
    void verify(String email, String code);
}
