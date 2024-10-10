package com.example.server.services;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.SmsResponse;

public interface SmsService {
    void sendSms(String to, String otp);
    SmsResponse validateOtp(UserRequest request);
    String generateOtp(String phoneNumber);
}
