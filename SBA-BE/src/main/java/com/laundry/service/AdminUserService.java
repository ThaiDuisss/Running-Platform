package com.laundry.service;

import com.laundry.dto.request.AdminCreateUserRequest;
import com.laundry.dto.request.AdminUpdateUserRequest;
import com.laundry.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    UserResponse createUser(AdminCreateUserRequest req);

    Page<UserResponse> getUsers(String keyword, Pageable pageable);

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, AdminUpdateUserRequest req);

    void deleteUser(Long id);
}