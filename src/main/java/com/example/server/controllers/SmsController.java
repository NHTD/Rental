package com.example.server.controllers;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.SmsResponse;
import com.example.server.repositories.UserRepository;
import com.example.server.services.SmsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SmsController {

    SmsService smsService;

    @GetMapping("/send-sms/{to}")
    public ResponseEntity<String> sendSms(@PathVariable("to") String to) {
        return ResponseEntity.status(HttpStatus.OK).body("Message sent successfully");
    }


    @PostMapping("/validate-otp")
    public ResponseEntity<SmsResponse> validateOtp(@RequestBody UserRequest request) {
        SmsResponse response = smsService.validateOtp(request);
        if("Successful".equals(response.getMessage())){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
