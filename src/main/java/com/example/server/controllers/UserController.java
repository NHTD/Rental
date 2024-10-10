package com.example.server.controllers;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.request.UserUpdateRequest;
import com.example.server.dtos.response.ResponseObject;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.exception.RentalHomeForbiddenException;
import com.example.server.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    ResponseEntity<ResponseObject> createUser(
            @RequestBody UserRequest request,
            @RequestParam(defaultValue = "") String loginType
    ) throws Exception {
        UserResponse userResponse = userService.createUser(request, UserStatusEnum.INVALID, loginType);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder().data(userResponse).build());
    }

    @PostMapping("/details")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<UserResponse> getUser(@RequestHeader("Authorization") String token) {
//        if (token == null || !token.startsWith("Bearer ")) {
//            throw new RentalHomeForbiddenException("Invalid token");
//        }

        String extractedToken = token.substring(7);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(extractedToken));
    }

    @PostMapping("/verify/{accountType}")
    ResponseEntity<String> verify(
            @PathVariable("accountType") String accountType,
            @RequestBody UserRequest request
    ) throws IOException {
        userService.verify(request.getOtp(), accountType);
        return ResponseEntity.status(HttpStatus.OK).body("User verified successfully");
    }

    @PutMapping("/{userId}")
    ResponseEntity<UserResponse> updateUser(
            @PathVariable("userId") String userId,
            @ModelAttribute UserUpdateRequest request,
            @RequestPart(value = "avatar", required = false) MultipartFile file
    ) throws Exception {
        UserResponse userResponse = userService.updateUser(userId, request);

        if (file != null && !file.isEmpty()) {
            String avatar = userService.uploadAvatar(userId, file);

            userResponse.setAvatar(avatar);
        }

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
