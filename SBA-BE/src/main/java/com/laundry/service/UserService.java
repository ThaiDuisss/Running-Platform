package com.laundry.service;

import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.UserResponse;

public interface UserService  {
    UserResponse register(UserRequest userRegister);
    UserResponse getUserById(Long userId);
    UserResponse getUserByUsername(String username);
    boolean verifyEmail(String token);
    void sendEmail(String email);
    void delete(Long id);
}
