package com.laundry.controller;


import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl service;

    @GetMapping("/get-info/{userId}")
    ApiResponse<UserResponse> getMyInfo(@PathVariable() Long userId) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .status("Get Information Successful")
                .data(service.getUserById(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ApiResponse.<UserResponse>builder()
                .data(service.getUserByUsername(username))
                .code(200)
                .status("Get Information Successful")
                .build();
    }
}
