package com.laundry.mapper;


import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserAuth.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUserProfile(UserRequest users);
    UserResponse toUserResponse(Users user);
}
