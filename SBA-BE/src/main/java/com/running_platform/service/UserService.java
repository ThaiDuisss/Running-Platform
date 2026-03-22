package com.running_platform.service;

import com.running_platform.dto.request.ResetPasswordRequest;
import com.running_platform.dto.request.UpdateAvatarRequest;
import com.running_platform.dto.request.UpdateProfileRequest;
import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;
import org.apache.catalina.User;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService  {
    UserResponse register(UserRequest userRegister);
    UserResponse getUserById(Long userId);
    UserResponse getUserByUsername(String username);
    boolean verifyEmail(String token);
    void sendEmail(String email);
    void delete(Long id);
    UserResponse updateUser (UserResponse userResponse);
    Optional<UserResponse> findOptionalUserByEmail(String email);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordRequest request);
    Page<UserResponse> getUsers(String keyword, Pageable pageable);
    UserResponse updateMyProfile(String username, UpdateProfileRequest request);
    UserResponse updateAvatar(String username, UpdateAvatarRequest request);
}
