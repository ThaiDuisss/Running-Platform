package com.running_platform.service;

import com.running_platform.dto.request.AdminCreateUserRequest;
import com.running_platform.dto.request.AdminUpdateUserRequest;
import com.running_platform.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface AdminUserService {

    UserResponse createUser(AdminCreateUserRequest req);

    Page<UserResponse> getUsers(String keyword, Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse updateUser(@PathVariable Long id,
                            @ModelAttribute AdminUpdateUserRequest req,
                            @RequestParam MultipartFile avatar);

    void deleteUser(Long id);

    UserResponse updateAvatar(long id, MultipartFile file);
}