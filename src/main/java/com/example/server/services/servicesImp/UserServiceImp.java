package com.example.server.services.servicesImp;

import com.example.server.dtos.request.UserCreationRequest;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.UserMapper;
import com.example.server.models.Role;
import com.example.server.models.User;
import com.example.server.repositories.RoleRepository;
import com.example.server.repositories.UserRepository;
import com.example.server.security.JwtConstant;
import com.example.server.services.MailService;
import com.example.server.services.UserService;
import com.example.server.utils.VelocityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImp implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    MailService mailService;

    @Override
    public UserResponse createUser(UserCreationRequest request, UserStatusEnum status) throws MessagingException, IOException {
        if(userRepository.findByAccountType(request.getAccountType()).isPresent()){
            throw new RentalHomeDataModelNotFoundException("Account already existed");
        }
        User user = userMapper.userToUser(request);
        if(status.equals(UserStatusEnum.INVALID)){
            user.setStatus(UserStatusEnum.INVALID);
        }
        user.setVerificationCode(VelocityUtil.generateVerificationCode());

        String password = !user.getAccountType().contains("@gmail") ? passwordEncoder.encode(request.getPassword())
                                                                    : request.getPassword();

        user.setPassword(password);

        Role role = roleRepository.findRoleByName("USER")
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User role is not found"));

        user.setRoles(Collections.singleton(role));

        Map<String, Object> emailParams = new HashMap<>();
        emailParams.put("name", user.getName());
        emailParams.put("link", "http://localhost:8080/rentalHome/users/verify?code=" + user.getVerificationCode() + "&email=" + user.getAccountType());

        mailService.sendTemplateEmail(request.getAccountType(), "Email Verification", emailParams, "Register.vm");

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUser(String token) {
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        boolean isTokenValid = claims.getExpiration().before(new Date());

        if(isTokenValid){
            throw new RentalHomeDataModelNotFoundException("Token is expired");
        }

        String accountType = String.valueOf(claims.get("accountType"));
        if(accountType.isEmpty()){
            throw new RentalHomeDataModelNotFoundException("Account type is not existed");
        }

        User user = userRepository.findByAccountType(accountType)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User is not existed"));

        return userMapper.userToUserResponse(user);
    }

    @Override
    public void verify(String email, String code) {
        User user = userRepository.findByAccountType(email)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User not found"));

        if(user.getStatus() == UserStatusEnum.VALID) {
            throw new RuntimeException("User already verified");
        }

        if(!StringUtils.equals(code, user.getVerificationCode())){
            throw new RuntimeException("Invalid verification code");
        }

        user.setStatus(UserStatusEnum.VALID);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}
