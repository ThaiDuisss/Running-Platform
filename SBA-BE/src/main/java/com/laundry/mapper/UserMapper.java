package com.laundry.mapper;


import com.laundry.dto.request.UserProfile;
import com.laundry.dto.request.UserRegister;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.UserAuth.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    UserProfile toUserProfile(UserRegister userRegister);
//    UserResponse toUserResponse(UserRegister user);
    UserResponse toUserResponse(Users user);
}
