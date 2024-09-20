package com.example.server.controllers;

import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.services.UserService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserCreationRequest request) throws MessagingException, IOException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(request, UserStatusEnum.INVALID));
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String token) {
        String extractedToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(extractedToken));
    }

    @GetMapping("/home")
    ResponseEntity<String> home() {
        return ResponseEntity.ok().body("home");
    }

    @GetMapping("/verify")
    ResponseEntity<String> verify(@RequestParam String code, @RequestParam String email) {
        userService.verify(email, code);
        return ResponseEntity.status(HttpStatus.OK).body("User verified successfully");
    }
}
