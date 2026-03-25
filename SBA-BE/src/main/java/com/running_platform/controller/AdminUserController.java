package com.running_platform.controller;

import com.running_platform.dto.request.AdminCreateUserRequest;
import com.running_platform.dto.request.AdminUpdateUserRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AdminUserController {
    final AdminUserService adminUserService;

    @PostMapping
    public ApiResponse<UserResponse> create(
            @RequestBody @Valid AdminCreateUserRequest req
    ) {
        UserResponse data = adminUserService.createUser(req);

        return ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("Create user successfully")
                .data(data)
                .build();
    }

    @GetMapping
    public ApiResponse<Page<UserResponse>> list(
            @RequestParam(defaultValue = "") String keyword,
            Pageable pageable
    ) {
        Page<UserResponse> data = adminUserService.getUsers(keyword, pageable);

        return ApiResponse.<Page<UserResponse>>builder()
                .status("SUCCESS")
                .code(200)
                .message("Get users successfully")
                .data(data)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> detail(@PathVariable Long id) {
        UserResponse data = adminUserService.getUserById(id);

        return ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("Get user successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> update(
            @PathVariable Long id,
            @ModelAttribute AdminUpdateUserRequest req,
            @RequestParam MultipartFile avatar
    ) {
        UserResponse data = adminUserService.updateUser(id, req,avatar);

        return ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("Update user successfully")
                .data(data)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);

        return ApiResponse.<Void>builder()
                .status("SUCCESS")
                .code(200)
                .message("Delete user successfully")
                .data(null)
                .build();
    }
    @PostMapping(value = "/{id}/avatar")
    public ApiResponse<UserResponse> uploadAvatar(
            @PathVariable Integer id,
            @RequestParam("file") MultipartFile file,
            @RequestParam String username,
            @RequestParam String phoneNumber,
            @RequestParam String role) {
        UserResponse data = adminUserService.updateAvatar(id, file);
        return ApiResponse.<UserResponse>builder()
                .status("SUCCESS")
                .code(200)
                .message("Update avt sucessfully")
                .data(data)
                .build();
    }

}
