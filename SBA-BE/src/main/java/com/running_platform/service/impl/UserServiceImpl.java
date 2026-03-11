package com.running_platform.service.impl;

import com.running_platform.config.AuthConfig;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.constant.RoleEnum;
import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.entity.UserAuth.Roles;
import com.running_platform.entity.UserAuth.Users;

import com.running_platform.exception.AppException;
import com.running_platform.mapper.RoleMapper;
import com.running_platform.mapper.UserMapper;
import com.running_platform.repository.RoleRepository;
import com.running_platform.repository.AuthRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl implements UserService {
    AuthRepository repository;
    RoleRepository roleRepository;
    UserMapper mapper;
    RoleMapper roleMapper;
    AuthConfig authConfig;
    JwtServiceImp jwtService;
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
                .password(authConfig.passwordEncoder().encode(userRegister.getPassword()))
                .roles(roles)
                .emailVerified(false)
                .fullName(userRegister.getFullName())
                .phoneNumber(userRegister.getPhoneNumber())
                .build();

        user = repository.save(user);
        Users userProfile = mapper.toUserProfile(userRegister);
        userProfile.setId(user.getId());
        UserResponse userResponse = mapper.toUserResponse(userProfile);
        userResponse.setId(user.getId());
        sendEmail(userRegister.getUsername());
        return userResponse;
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
    public Page<UserResponse> getUsers(String keyword, Pageable pageable) {

        Page<Users> page = userRepository
                .findByUsernameContainingIgnoreCase(keyword, pageable);

        return page.map(mapper::toUserResponse);
    }

}
