package com.example.server.services.servicesImp;

import com.example.server.dtos.request.AuthenticationRequest;
import com.example.server.dtos.request.PasswordRequest;
import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.response.AuthenticationResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.exception.RentalHomeDataInvalidException;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.models.User;
import com.example.server.repositories.UserRepository;
import com.example.server.security.JwtProvider;
import com.example.server.security.UserDetailsServiceImp;
import com.example.server.services.AuthenticationService;
import com.example.server.services.MailService;
import com.example.server.services.SmsService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.service.SecurityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationServiceImp implements AuthenticationService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String facebookRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.auth-uri}")
    private String facebookAuthUri;

    final UserDetailsServiceImp userDetailsService;
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final MailService mailService;
    final SmsService smsService;

    @Override
    public AuthenticationResponse signIn(AuthenticationRequest request) {
        User user = userRepository.findByAccountType(request.getAccountType())
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Tài khoản không tồn tại"));

        if(user.getStatus().equals(UserStatusEnum.INVALID)) {
            throw new RentalHomeDataInvalidException("Tài khoản này chưa được xác thực");
        }

        Authentication authentication = authenticate(request.getAccountType(), request.getPassword());

        String jwt = JwtProvider.generateToken(authentication);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    private Authentication authenticate(String accountType, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountType);

        if(!userDetails.getUsername().equals(accountType)){
            throw new RentalHomeDataInvalidException("This account is not registered", accountType);
        }

        if(password != null){
            if(!passwordEncoder.matches(password, userDetails.getPassword())){
                throw new RentalHomeDataInvalidException("Invalid password");
            }
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public String generateUrl(String loginType) {
        String url="";
        loginType.trim().toLowerCase();

        if("google".equals(loginType)) {
            url = new GoogleAuthorizationCodeRequestUrl(googleClientId, googleRedirectUri, Arrays.asList("email", "profile", "openid")).build();
        } else if ("facebook".equals(loginType)) {
            /*
            url = String.format("https://www.facebook.com/v3.2/dialog/oauth?client_id=%s&redirect_uri=%s&scope=email,public_profile&response_type=code",
                    facebookClientId, facebookRedirectUri);
             */
            url = UriComponentsBuilder
                    .fromUriString(facebookAuthUri)
                    .queryParam("client_id", facebookClientId)
                    .queryParam("redirect_uri", facebookRedirectUri)
                    .queryParam("scope", "email,public_profile")
                    .queryParam("response_type", "code")
                    .build()
                    .toUriString();
        }

        return url;
    }

    @Override
    public Map<String, Object> authenticateAndFetchProfile(String code, String loginType) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String accessToken;
        String url;
        Gson gson = new Gson();

        switch (loginType.toLowerCase()) {
            case "google":
                accessToken = new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(), new GsonFactory(),
                        googleClientId,
                        googleClientSecret,
                        code,
                        googleRedirectUri
                ).execute().getAccessToken();

                // Set the URL for the Google API to fetch user info
                url = "https://www.googleapis.com/oauth2/v3/userinfo";
                break;

            case "facebook":
                url = UriComponentsBuilder
                        .fromUriString("https://graph.facebook.com/v20.0/oauth/access_token")
                        .queryParam("client_id", facebookClientId)
                        .queryParam("redirect_uri", facebookRedirectUri)
                        .queryParam("client_secret", facebookClientSecret)
                        .queryParam("code", code)
                        .toUriString();

                // Use RestTemplate to fetch the Facebook access token
                Map<String, Object> response = gson.fromJson(restTemplate.getForObject(url, String.class), Map.class);
                accessToken = (String) response.get("access_token");

                // Set the URL for the Facebook API to fetch user info
                url = "https://graph.facebook.com/me?fields=id,name,first_name,last_name,email,picture.width(250).height(250)&access_token=" + accessToken;
                break;

            default:
                System.out.println("Unsupported login type: " + loginType);
                return null;
        }

        // Configure RestTemplate to include the access token in the Authorization header
        restTemplate.getInterceptors().add((req, body, executionContext) -> {
            req.getHeaders().set("Authorization", "Bearer " + accessToken);
            return executionContext.execute(req, body);
        });

        // Make a GET request to fetch user information
        String userInfoResponse = restTemplate.getForObject(url, String.class);
        return gson.fromJson(userInfoResponse, Map.class);
    }

    @Override
    public void changePassword(String accountType, PasswordRequest request) {
        User user = userRepository.findByAccountType(accountType)
                .orElseThrow(() -> new RentalHomeDataInvalidException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RentalHomeDataInvalidException("Invalid old password");
        }

        String newPasswordEncoded = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(newPasswordEncoded);

        userRepository.save(user);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getAccountType());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Override
    public void verifyAccount(String account) throws MessagingException, IOException {
        User user = userRepository.findByAccountType(account)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("The {} account is not existed", account));
        String otp = smsService.generateOtp(account);
        user.setOtp(otp);
        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("name", user.getName());
        emailParams.put("link", "http://localhost:3000/tao-mat-khau-moi/" + user.getAccountType());
        user.setOtp(otp);
        userRepository.save(user);

        mailService.sendTemplateEmail(account, "Email Verification", emailParams, "ForgotPassword.html");
    }
}
