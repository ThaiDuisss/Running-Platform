package com.running_platform.mapper;


import com.running_platform.constant.RoleEnum;
import com.running_platform.dto.request.AdminCreateUserRequest;
import com.running_platform.dto.request.AdminUpdateUserRequest;
import com.running_platform.dto.request.UserRequest;
import com.running_platform.dto.response.UserResponse;
import com.running_platform.entity.UserAuth.Roles;
import com.running_platform.entity.UserAuth.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {
    Users toUserProfile(UserRequest users);
    @Mapping(source = "id", target = "id")
    UserResponse toUserResponse(Users user);
    Users toUser(AdminCreateUserRequest req);
    Users responseToUserProfile(UserResponse users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Users user, AdminUpdateUserRequest req
    );

    default Roles map(RoleEnum role) {
        Roles r = new Roles();
        r.setRoleName(role);
        return r;
    }
}
