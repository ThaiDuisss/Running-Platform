package com.laundry.mapper;


import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<Users, UserRequest, UserResponse> {
    UserProfile toUserProfile(UserRegister userRegister);
    UserResponse toUserResponse(UserRegister user);
    ProfileResponse toProfileResponse(UserProfile user);
}
