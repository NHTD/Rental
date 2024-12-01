package com.example.server.services.servicesImp;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.SmsResponse;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.models.User;
import com.example.server.repositories.UserRepository;
import com.example.server.services.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmsServiceImp implements SmsService {
    @Value("${spring.twilio.account-sid}")
    String accountSid;

    @Value("${spring.twilio.auth-token}")
    String authToken;

    @Value("${spring.twilio.phone-number-trial}")
    String phoneNumberTrial;

    final UserRepository userRepository;


    @Override
    public void sendSms(String to, String otp) {
        Twilio.init(accountSid, authToken);
        Message.creator(new PhoneNumber(to), new PhoneNumber(phoneNumberTrial), "Your OTP code is: " + otp).create();
    }

    @Override
    public SmsResponse validateOtp(UserRequest request) {
        User user = userRepository.findByAccountType(request.getAccountType())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Failed to get user with phone number: {}", request.getAccountType()));

        if(user.getAccountType().equals(request.getAccountType())) {
            if(user.getOtp().equals(request.getOtp())) {
                user.setOtp("");
                userRepository.save(user);
                return SmsResponse.builder().message("Successful").build();
            }
        }
        return SmsResponse.builder().message("Fail").build();
    }

    @Override
    public String generateOtp(String phoneNumber) {
        Random random = new Random();
        return String.valueOf(random.nextInt(999999));
    }
}
