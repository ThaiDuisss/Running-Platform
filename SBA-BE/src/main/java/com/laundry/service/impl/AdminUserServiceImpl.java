package com.laundry.service.impl;

import com.laundry.dto.request.AdminCreateUserRequest;
import com.laundry.dto.request.AdminUpdateUserRequest;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserAuth.Roles;
import com.laundry.entity.UserAuth.Users;
import com.laundry.mapper.UserMapper;
import com.laundry.repository.RoleRepository;
import com.laundry.repository.UserRepository;
import com.laundry.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(AdminCreateUserRequest req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Users user = userMapper.toUser(req);

        user.setPassword(passwordEncoder.encode(req.getPassword()));

        Set<Roles> roles = roleRepository.findByRoleNameIn(req.getRoles());
        user.setRoles(roles);

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserResponse> getUsers(String keyword, Pageable pageable) {

        Page<Users> page = userRepository
                .findByUsernameContainingIgnoreCase(keyword, pageable);

        return page.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse getUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, AdminUpdateUserRequest req) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, req);

        if (req.getRoles() != null) {
            Set<Roles> roles = roleRepository.findByRoleNameIn(req.getRoles());
            user.setRoles(roles);
        }

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsDeleted(true);
        userRepository.save(user);
    }
}