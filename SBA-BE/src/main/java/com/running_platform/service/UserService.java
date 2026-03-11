package com.running_platform.service;

import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService  {
    UserResponse register(UserRequest userRegister);
    UserResponse getUserById(Long userId);
    UserResponse getUserByUsername(String username);
    boolean verifyEmail(String token);
    void sendEmail(String email);
    void delete(Long id);
    Page<UserResponse> getUsers(String keyword, Pageable pageable);
}
