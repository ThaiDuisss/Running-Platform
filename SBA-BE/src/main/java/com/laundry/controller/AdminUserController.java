package com.laundry.controller;

import com.laundry.dto.request.AdminCreateUserRequest;
import com.laundry.dto.request.AdminUpdateUserRequest;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        UserResponse data = adminUserService.getUser(id);

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
            @RequestBody AdminUpdateUserRequest req
    ) {
        UserResponse data = adminUserService.updateUser(id, req);

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
}
