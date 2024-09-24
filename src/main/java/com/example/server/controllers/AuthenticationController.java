package com.example.server.controllers;

import com.example.server.dtos.request.AuthenticationRequest;
import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.AuthenticationResponse;
import com.example.server.dtos.response.ResponseObject;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.models.User;
import com.example.server.repositories.UserRepository;
import com.example.server.services.AuthenticationService;
import com.example.server.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//        picture = (String) userInfo.get("picture");
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
        UserCreationRequest userCreationRequest = UserCreationRequest.builder()
                        .name(name)
                        .accountType(email)
                        .password("")
                        .build();

        if (loginType.trim().equals("google")) {
            userCreationRequest.setGoogleAccountId(googleAccountId);
        }else if (loginType.trim().equals("facebook")) {
            userCreationRequest.setFacebookAccountId(facebookAccountId);
        }

        Optional<User> user = userRepository.findByAccountType(email);
        if(!user.isPresent()){
            userService.createUser(userCreationRequest, UserStatusEnum.INVALID);
        }

        AuthenticationResponse authResponse = authenticationService.signIn(authenticationRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("Login successful", HttpStatus.OK, authResponse));
    }
}
