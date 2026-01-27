package com.laundry.service.impl;

import com.laundry.config.AuthConfig;

import com.laundry.constant.ErrorEnum;
import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Roles;
import com.laundry.entity.Users;

import com.laundry.exception.AppException;
import com.laundry.mapper.RoleMapper;
import com.laundry.mapper.UserMapper;
import com.laundry.repository.RoleRepository;
import com.laundry.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl {
    AuthRepository repository;
    RoleRepository roleRepository;
    UserMapper mapper;
    RoleMapper roleMapper;
    AuthConfig authConfig;
    JwtServiceImp jwtService;

    @Transactional
    public UserResponse register(UserRegister userRegister) {
        if (repository.existsByUsername(userRegister.getUsername())) {
            throw new AppException(ErrorEnum.USERNAME_EXIST);
        }
        Set<Roles> roles = roleRepository.findByRoleNameIn(userRegister.getRoles());
        Users user = Users.builder()
                .username(userRegister.getUsername())
                .password(authConfig.passwordEncoder().encode(userRegister.getPassword()))
                .roles(roles)
                .emailVerified(false)
                .build();

        user = repository.save(user);
        UserProfile userProfile = mapper.toUserProfile(userRegister);
        userProfile.setUserId(user.getId());
        userProfile.setUserId(user.getId());
        UserResponse userResponse = mapper.toUserResponse(userRegister);
        userResponse.setId(user.getId());
        sendEmail(userRegister.getUsername());
        return userResponse;
    }

//    public ProfileResponse getMyInfo(Long userId) {
//        Users user = repository.findById(userId).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
//        ProfileResponse profileResponse = mapper.toProfileResponse(users);
//        profileResponse.setRole(roleMapper.toResponse(user.getRoles()));
//        profileResponse.setUsername(user.getUsername());
//        return profileResponse;
//    }

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

}
