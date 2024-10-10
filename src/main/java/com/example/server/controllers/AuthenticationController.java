package com.example.server.controllers;

import com.example.server.dtos.request.AuthenticationRequest;
import com.example.server.dtos.request.PasswordRequest;
import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.AuthenticationResponse;
import com.example.server.dtos.response.ResponseObject;
import com.example.server.enums.UserStatusEnum;
import com.example.server.models.User;
import com.example.server.repositories.UserRepository;
import com.example.server.services.AuthenticationService;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationController {

    final AuthenticationService authenticationService;
    final UserService userService;
    final UserRepository userRepository;

    @PostMapping
    ResponseEntity<ResponseObject> signIn(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.signIn(request);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder().message("Login successfully").data(authenticationResponse).build());
    }

    @GetMapping("/social-login")
    public ResponseEntity<String> socialAuth(@RequestParam("login_type") String loginType){
        loginType = loginType.trim().toLowerCase();
        String url = authenticationService.generateUrl(loginType);
        return ResponseEntity.ok(url);
    }

    @PutMapping("/change-password/{accountType}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> changePassword(@PathVariable("accountType") String accountType, @RequestBody PasswordRequest request){
        authenticationService.changePassword(accountType, request);
        return ResponseEntity.status(HttpStatus.OK).body("Successful");
    }

    @GetMapping("/social/callback")
    public ResponseEntity<ResponseObject> callback(
            @RequestParam("code") String code,
            @RequestParam("login_type") String loginType
    ) throws Exception {

        //Get user info
        Map<String, Object> userInfo = authenticationService.authenticateAndFetchProfile(code, loginType);

        if (userInfo == null) {
            return ResponseEntity.badRequest().body(new ResponseObject(
                    "Failed to authenticate", HttpStatus.BAD_REQUEST, null
            ));
        }
        String picture = "";

        String googleAccountId = (String) userInfo.get("sub");
        String facebookAccountId = (String) userInfo.get("id");
        String name = (String) userInfo.get("name");
//        String givenName = (String) userInfo.get("given_name");
//        String familyName = (String) userInfo.get("family_name");
        if("google".equals(loginType)) {
            picture = (String) userInfo.get("picture");
        }
//        if("facebook".equals(loginType)){
//            picture = (String) userInfo.get("picture");
//        }

        String email = (String) userInfo.get("email");
//        Boolean emailVerified = (Boolean) userInfo.get("email_verified");
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email(email)
                .accountType(email)
                .profileImage(picture)
                .build();
        UserRequest userRequest = UserRequest.builder()
                        .name(name)
                        .accountType(email)
                        .password("")
                        .build();

        if (loginType.trim().equals("google")) {
            userRequest.setGoogleAccountId(googleAccountId);
        }else if (loginType.trim().equals("facebook")) {
            userRequest.setFacebookAccountId(facebookAccountId);
        }

        Optional<User> user = userRepository.findByAccountType(email);
        if(!user.isPresent()){
            userService.createUser(userRequest, UserStatusEnum.INVALID, loginType);
        }

        AuthenticationResponse authResponse = authenticationService.signIn(authenticationRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("Login successful", HttpStatus.OK, authResponse));
    }

    @PostMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestBody UserRequest request) throws MessagingException, IOException {
        authenticationService.verifyAccount(request.getAccountType());

        return ResponseEntity.status(HttpStatus.OK).body("Successful");
    }

    @PostMapping("/verify-from-password")
    ResponseEntity<String> verifyFromForgetPassword(@RequestBody UserRequest request) throws IOException {
        userService.verifyFromForgotPassword(request.getOtp());
        return ResponseEntity.status(HttpStatus.OK).body("User verified successfully");
    }
}
