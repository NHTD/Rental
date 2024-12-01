package com.example.server.services;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.request.UserUpdateRequest;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserResponse createUser(UserRequest request, UserStatusEnum status, String loginType) throws MessagingException, IOException;
    UserResponse getUser(String id);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void verify(String otp, String accountType) throws IOException;
    void verifyFromForgotPassword(String otp) throws IOException;
    String uploadAvatar(String userId, MultipartFile file) throws Exception;
}
