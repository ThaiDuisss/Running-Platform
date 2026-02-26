package com.laundry.mapper;


import com.laundry.dto.request.AdminCreateUserRequest;
import com.laundry.dto.request.AdminUpdateUserRequest;
import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserAuth.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUserProfile(UserRequest users);
    @Mapping(source = "id", target = "userId")
    UserResponse toUserResponse(Users user);
    Users toUser(AdminCreateUserRequest req);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Users user, AdminUpdateUserRequest req
    );
}
