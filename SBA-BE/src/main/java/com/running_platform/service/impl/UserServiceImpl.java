package com.running_platform.service.impl;

import com.running_platform.config.AuthConfig;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.constant.RoleEnum;
import com.running_platform.dto.request.ResetPasswordRequest;
import com.running_platform.dto.request.UpdateAvatarRequest;
import com.running_platform.dto.request.UpdateProfileRequest;
import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.entity.UserAuth.PasswordResetTokens;
import com.running_platform.entity.UserAuth.Roles;
import com.running_platform.entity.UserAuth.Users;

import com.running_platform.entity.UserAuth.VerificationTokens;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.RoleMapper;
import com.running_platform.mapper.UserMapper;
import com.running_platform.repository.*;
import com.running_platform.service.EmailService;
import com.running_platform.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl implements UserService {
    AuthRepository repository;
    RoleRepository roleRepository;
    UserMapper mapper;
    RoleMapper roleMapper;
    JwtServiceImp jwtService;
    PasswordEncoder passwordEncoder;
    VerificationTokenRepository tokenRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    EmailService emailService;
    UserRepository userRepository;

    @Transactional
    public UserResponse register(UserRequest userRegister) {
        if (repository.existsByUsername(userRegister.getUsername())) {
            throw new AppException(ErrorEnum.USERNAME_EXIST);
        }
        Set<Roles> roles = new HashSet<>();
        Roles roleUser = roleRepository.findByRoleName(RoleEnum.USER);
        roles.add(roleUser);
        Users user = Users.builder()
                .username(userRegister.getUsername())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .roles(roles)
                .emailVerified(false)
                .registeredProviderName(userRegister.getRegisteredProviderName())
                .registeredProviderId(userRegister.getRegisteredProviderId())
                .fullName(userRegister.getFullName())
                .emailVerified(userRegister.isEmailVerified())
                .phoneNumber(userRegister.getPhoneNumber())
                .build();

        user = repository.save(user);

        if (!userRegister.isEmailVerified()) {
            sendVerificationLink(user);
        }


        Users userProfile = mapper.toUserProfile(userRegister);
        userProfile.setId(user.getId());
        return mapper.toUserResponse(userProfile);
    }

    private void sendVerificationLink(Users user) {
        String token = UUID.randomUUID().toString();

        VerificationTokens verificationToken = VerificationTokens.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(verificationToken);
        String content = "Click this link to verify: " + "http://localhost:8080/auth/verify?token=" + token;
        String subject = "Email Verification";
        emailService.sendVerificationEmail(user.getUsername(), content, subject);
    }

    public UserResponse getUserById(Long userId) {
        Users user = repository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        UserResponse userResponse = mapper.toUserResponse(user);
        userResponse.setRoles(roleMapper.toResponse(user.getRoles()));
        return userResponse;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        Users user = repository.findByUsername(username).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        UserResponse userResponse = mapper.toUserResponse(user);
        userResponse.setRoles(roleMapper.toResponse(user.getRoles()));
        return userResponse;
    }

    public boolean verifyEmail(String token) {
        try {
            log.info("Verifying email with token: {}", token);
            String email = jwtService.extractTokenEmail(token);
            log.info("email:{}", email);
            Users user = repository.findByUsername(email)
                    .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

            user.setEmailVerified(true);
            repository.save(user);
//            NotificationEvent notificationEvent = NotificationEvent.builder()
//                    .channel("Validate-EMAIL")
//                    .subject("Validate Successfully")
//                    .body(email)
//                    .recipient(user.getUsername())
//                    .build();
//
//            kafkaTemplate.send("validate-email", notificationEvent);
            return true;
        } catch (AppException ex) {
            return false;
        }
    }

    public void sendEmail(String email) {
        String token = jwtService.generateEmailToken(email);
//        NotificationEvent notificationEvent = NotificationEvent.builder()
//                .channel("EMAIL")
//                .subject("Welcome to Laundry Service")
//                .body(token)
//                .recipient(email)
//                .build();
//        //Publish massage to kafka
//        kafkaTemplate.send("notification-delivery", notificationEvent);
    }


    public void delete(Long id) {
        Users users = repository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        repository.delete(users);
    }

    @Override
    public void forgotPassword(String email) {

        Optional<Users> optionalUser = repository.findByUsername(email);
        if (optionalUser.isEmpty()) {
            return;
        }
        Users user = optionalUser.get();
        PasswordResetTokens token = passwordResetTokenRepository.findByUser(user);
        if (token != null) {
            if (token.getExpiryDate().isAfter(LocalDateTime.now())) {
                return;
            }
            passwordResetTokenRepository.delete(token);
        }

        sendResetPasswordLink(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetTokens passwordResetTokens = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppException(ErrorEnum.INVALID_TOKEN));
        Users user = passwordResetTokens.getUser();
        if (passwordResetTokens.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorEnum.EXPIRED_TOKEN);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        passwordResetTokenRepository.delete(passwordResetTokens);
        repository.save(user);
    }

    private void sendResetPasswordLink(Users user) {
        String token = UUID.randomUUID().toString();

        PasswordResetTokens passwordResetTokens = PasswordResetTokens.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(passwordResetTokens);
        String content = "Click link to reset password: " + "http://localhost:5173/reset-password?token=" + token;
        String subject = "Reset password";
        emailService.sendVerificationEmail(user.getUsername(), content, subject);
    }

    @Override
    public Page<UserResponse> getUsers(String keyword, Pageable pageable) {

        Page<Users> page = userRepository
                .findByUsernameContainingIgnoreCase(keyword, pageable);

        return page.map(mapper::toUserResponse);
    }

    @Override
    public UserResponse updateMyProfile(String username, UpdateProfileRequest request) {
        Users userEntity = repository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        userEntity.setFullName(request.getFullName());
        userEntity.setPhoneNumber(request.getPhoneNumber());
        userEntity.setAddress(request.getAddress());
        if (request.getLatitude() != null && request.getLongitude() != null) {
            String point = String.format("POINT(%s %s)", request.getLongitude(), request.getLatitude());
            userEntity.setLocation(point);
        }
        if (request.getImageUrl() != null) {
            userEntity.setImageUrl(request.getImageUrl());
        }

        repository.save(userEntity);

        UserResponse userResponse = mapper.toUserResponse(userEntity);
        userResponse.setRoles(roleMapper.toResponse(userEntity.getRoles()));
        return userResponse;
    }

    @Override
    public UserResponse updateAvatar(String username, UpdateAvatarRequest request) {
        Users userEntity = repository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        userEntity.setImageUrl(request.getImageUrl());
        repository.save(userEntity);

        UserResponse userResponse = mapper.toUserResponse(userEntity);
        userResponse.setRoles(roleMapper.toResponse(userEntity.getRoles()));
        return userResponse;
    }

    @Override
    public UserResponse updateUser(UserResponse userResponse) {
        Users userEntity = repository.findById(userResponse.getId())
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        userEntity.setFullName(userResponse.getFullName());
        userEntity.setImageUrl(userResponse.getImageUrl());
        userEntity.setPhoneNumber(userResponse.getPhoneNumber());
        repository.save(userEntity);
        return mapper.toUserResponse(userEntity);
    }

    @Override
    public Optional<UserResponse> findOptionalUserByEmail(String email) {
        return repository.findByUsername(email).map(mapper::toUserResponse);
    }

}
