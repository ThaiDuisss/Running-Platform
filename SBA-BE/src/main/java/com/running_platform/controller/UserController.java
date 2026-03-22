package com.running_platform.controller;


import com.running_platform.dto.request.UpdateProfileRequest;
import com.running_platform.dto.request.UpdateAvatarRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
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

    @PutMapping("/my-info")
    ApiResponse<UserResponse> updateMyInfo(@RequestBody UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<UserResponse>builder()
                .data(service.updateMyProfile(username, request))
                .code(200)
                .status("Update Information Successful")
                .message("Profile updated successfully")
                .build();
    }

    @PutMapping("/my-info/avatar")
    ApiResponse<UserResponse> updateMyAvatar(@RequestBody UpdateAvatarRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ApiResponse.<UserResponse>builder()
                .data(service.updateAvatar(username, request))
                .code(200)
                .status("Update Avatar Successful")
                .message("Avatar updated successfully")
                .build();
    }

    // chua lam fe ket ban
    @GetMapping("/search")
    public ApiResponse<Page<UserResponse>> list(
            @RequestParam(defaultValue = "") String keyword,
            Pageable pageable
    ) {
        Page<UserResponse> data = service.getUsers(keyword, pageable);

        return ApiResponse.success(
                "Users search successfully",
                data
        );
    }

}
