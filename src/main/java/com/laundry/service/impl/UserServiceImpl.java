package com.laundry.service.impl;

import com.laundry.config.AuthConfig;
import com.laundry.dto.DTO.NotificationEvent;
import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Roles;
import com.laundry.entity.Users;
import com.laundry.enums.ErrorEnum;
import com.laundry.exception.AppException;
import com.laundry.mapper.RoleMapper;
import com.laundry.mapper.UserMapper;
import com.laundry.repository.RoleRepository;
import com.laundry.repository.AuthRepository;
import com.laundry.client.UserClient;
import com.laundry.service.UserService;
import com.laundry.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl extends BaseServiceImp<Users, Long, UserRequest, UserResponse, UserMapper, AuthRepository> implements UserService {

    UserClient userClient;
    AuthConfig authConfig;
    RoleRepository roleRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    JwtServiceImp jwtService;
    RoleMapper roleMapper;

    public UserServiceImpl(UserMapper mapper, AuthRepository repository, UserClient userClient, AuthConfig authConfig, RoleRepository roleRepository, KafkaTemplate<String, Object> kafkaTemplate, JwtServiceImp jwtService, RoleMapper roleMapper) {
        super(mapper, repository);
        this.userClient = userClient;
        this.authConfig = authConfig;
        this.roleRepository = roleRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.jwtService = jwtService;
        this.roleMapper = roleMapper;
    }

    @Transactional
    @Override
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
        userClient.createUser(userProfile);
        UserResponse userResponse = mapper.toUserResponse(userRegister);
        userResponse.setId(user.getId());
        sendEmail(userRegister.getUsername());
        return userResponse;
    }

    @Override
    public ProfileResponse getMyInfo(Long userId) {
        if (userId == null) {
            userId = SecurityUtils.getIdFromHeader();
            log.info("userId: {} ", userId);
        }
        Users user = repository.findById(userId).orElseThrow(() -> new AppException(getNotFoundError()));
        UserProfile users = userClient.getByUserId(user.getId()).getBody().getData();
        ProfileResponse profileResponse = mapper.toProfileResponse(users);
        profileResponse.setRole(roleMapper.toResponse(user.getRoles()));
        profileResponse.setUsername(user.getUsername());
        return profileResponse;
    }

    public boolean verifyEmail(String token) {
        try {
            log.info("Verifying email with token: {}", token);
            String email = jwtService.extractTokenEmail(token);
            log.info("email:{}", email);
            Users user = repository.findByUsername(email)
                    .orElseThrow(() -> new AppException(getNotFoundError()));

            user.setEmailVerified(true);
            repository.save(user);
            NotificationEvent notificationEvent = NotificationEvent.builder()
                    .channel("Validate-EMAIL")
                    .subject("Validate Successfully")
                    .body(email)
                    .recipient(user.getUsername())
                    .build();

            kafkaTemplate.send("validate-email", notificationEvent);
            return true;
        } catch (AppException ex) {
            return false;
        }
    }

    public void sendEmail(String email) {
        String token = jwtService.generateEmailToken(email);
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .subject("Welcome to Laundry Service")
                .body(token)
                .recipient(email)
                .build();
        //Publish massage to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);
    }

    @Override
    protected ErrorEnum getNotFoundError() {
        return ErrorEnum.USER_NOT_FOUND;
    }

    @Override
    public void delete(Long id) {
        userClient.deleteUser(id);
        Users users = repository.findById(id).orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        repository.delete(users);
    }

}
