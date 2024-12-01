package com.example.server.services.servicesImp;

import com.example.server.dtos.request.UserRequest;
import com.example.server.dtos.request.UserUpdateRequest;
import com.example.server.dtos.response.CloudinaryResponse;
import com.example.server.dtos.response.UserResponse;
import com.example.server.enums.UserStatusEnum;
import com.example.server.exception.RentalHomeDataModelNotFoundException;
import com.example.server.mapper.UserMapper;
import com.example.server.models.Role;
import com.example.server.models.User;
import com.example.server.repositories.RoleRepository;
import com.example.server.repositories.UserRepository;
import com.example.server.security.JwtConstant;
import com.example.server.services.CloudinaryService;
import com.example.server.services.MailService;
import com.example.server.services.SmsService;
import com.example.server.services.UserService;
import com.example.server.utils.FileUploadUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImp implements UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final CloudinaryService cloudinaryService;

    final UserMapper userMapper;

    final PasswordEncoder passwordEncoder;

    final SmsService smsService;
    final MailService mailService;

    @Value("${spring.website.fe-url}")
    String feUrl;

    @Override
    public UserResponse createUser(UserRequest request, UserStatusEnum status, String loginType) throws MessagingException, IOException {
        if(userRepository.findByAccountType(request.getAccountType()).isPresent()){
            throw new RentalHomeDataModelNotFoundException("Account already existed");
        }

        User user = userMapper.userToUser(request);
        if(status.equals(UserStatusEnum.INVALID)){
            user.setStatus(UserStatusEnum.INVALID);
        }

        if("google".equals(loginType) || "facebook".equals(loginType) || request.getAccountType().contains("@gmail")) {
            user.setEmail(request.getAccountType());
        } else {
            user.setPhone(request.getAccountType());
        }

//        user.setVerificationCode(VelocityUtil.generateVerificationCode());

        String password = "";

        if("google".equals(loginType) || "facebook".equals(loginType)){
            password = request.getPassword();
        }else if(request.getAccountType().contains("@gmail")){
            password = passwordEncoder.encode(request.getPassword());
            String otp = smsService.generateOtp(request.getAccountType());
            user.setOtp(otp);
            Map<String, Object> emailParams = new HashMap<>();
            emailParams.put("name", user.getName());
            emailParams.put("otp", user.getOtp());
            emailParams.put("link", feUrl + "/otp/" + user.getAccountType());

            mailService.sendTemplateEmail(request.getAccountType(), "Email Verification", emailParams, "Register.html");
        }else{
            String otp = smsService.generateOtp(request.getAccountType());
            user.setOtp(otp);

            password = passwordEncoder.encode(request.getPassword());
            smsService.sendSms("+18"+request.getAccountType(), otp);
        }

        user.setPassword(password);

        Role role = roleRepository.findRoleByName("USER")
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User role is not found"));

        user.setRoles(Collections.singleton(role));

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
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RentalHomeDataModelNotFoundException("This id is not found"));

        userMapper.userToUserUpdate(user, request);
        if(!request.getEmail().equals(user.getAccountType())){
            user.setAccountType(request.getEmail());
        }

        if(user.getAccountType().contains("@gmail")){
            user.setPhone("");
        }

        return userMapper.userToUserResponse(userRepository.save(user));
    }

    @Override
    public void verify(String otp, String accountType) {
        User user = userRepository.findByAccountType(accountType)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Tài khoản không tồn tại"));

        if(user.getStatus() == UserStatusEnum.VALID) {
            throw new RuntimeException("Người dùng đã được xác thực rồi");
        }

        if(!StringUtils.equals(otp, user.getOtp())){
            userRepository.deleteById(user.getId());
            throw new RuntimeException("Otp không chính xác");
        }

        user.setStatus(UserStatusEnum.VALID);
        user.setOtp(null);
        userRepository.save(user);
    }

    @Override
    public void verifyFromForgotPassword(String otp) {
        User user = userRepository.findByOtp(otp)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("Otp is not found"));

        if(user.getStatus() == UserStatusEnum.VALID) {
            throw new RuntimeException("Người dùng đã được xác thực rồi");
        }

        if(!StringUtils.equals(otp, user.getOtp())){
            throw new RuntimeException("Otp không chính xác");
        }

        user.setStatus(UserStatusEnum.VALID);
        user.setOtp(null);
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(String userId, MultipartFile file) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RentalHomeDataModelNotFoundException("User not found"));

        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        String fileName = FileUploadUtils.getFilename(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
        user.setAvatar(response.getUrl());
        userRepository.save(user);

        return response.getUrl();
    }
}
