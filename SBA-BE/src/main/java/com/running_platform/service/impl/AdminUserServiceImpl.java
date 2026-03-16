package com.running_platform.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.running_platform.dto.request.AdminCreateUserRequest;
import com.running_platform.dto.request.AdminUpdateUserRequest;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.entity.UserAuth.Roles;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.UploadFolder;
import com.running_platform.mapper.UserMapper;
import com.running_platform.repository.RoleRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.AdminUserService;
import com.running_platform.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;
    private final Cloudinary cloudinary;

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

        return page.map(user -> {

            UserResponse response = userMapper.toUserResponse(user);

            if (user.getImageUrl() != null) {

                String avatar = cloudinary.url()
                        .transformation(new Transformation()
                                .width(100)
                                .height(100)
                                .crop("fill")
                                .quality("auto")
                                .fetchFormat("auto"))
                        .generate(UploadFolder.AVATAR_USER.getPath()+"/"+"user_" + user.getId());

                response.setImageUrl(avatar);
            }

            return response;
        });
    }
    @Override
    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser( Long id,
                                    AdminUpdateUserRequest req,
                                    MultipartFile avatar) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, req);

        Roles role = roleRepository.findByRoleName(req.getRole());
        if (role == null) {
            throw new RuntimeException("Role not found: " + req.getRole());
        }

        user.setRoles(new HashSet<>(List.of(role)));

        String imageURL = cloudinaryService.uploadFile(avatar,UploadFolder.AVATAR_USER,id);
        user.setImageUrl(imageURL);
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
    @Override
    public UserResponse updateAvatar(long id, MultipartFile file){
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String oldImage = user.getImageUrl();
        if(oldImage != null){
            String publicId = "user_"+id;
            cloudinaryService.deleteImg(publicId);
        }
        String uploadResult = cloudinaryService.uploadFile(file, UploadFolder.AVATAR_USER,id);
        user.setImageUrl(uploadResult);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}