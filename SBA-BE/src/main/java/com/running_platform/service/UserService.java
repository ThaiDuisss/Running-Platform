package com.running_platform.service;

import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;

public interface UserService  {
    UserResponse register(UserRequest userRegister);
    UserResponse getUserById(Long userId);
    UserResponse getUserByUsername(String username);
    boolean verifyEmail(String token);
    void sendEmail(String email);
    void delete(Long id);
}
