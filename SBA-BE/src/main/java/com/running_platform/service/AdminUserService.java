package com.running_platform.service;

import com.running_platform.dto.request.AdminCreateUserRequest;
import com.running_platform.dto.request.AdminUpdateUserRequest;
import com.running_platform.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    UserResponse createUser(AdminCreateUserRequest req);

    Page<UserResponse> getUsers(String keyword, Pageable pageable);

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, AdminUpdateUserRequest req);

    void deleteUser(Long id);
}