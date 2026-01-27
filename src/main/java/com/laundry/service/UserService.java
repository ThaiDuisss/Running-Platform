package com.laundry.service;

import com.laundry.dto.request.UserRegister;
import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Users;

public interface UserService extends BaseService<Users, Long, UserRequest, UserResponse> {
    UserResponse register(UserRegister userRegister);
    ProfileResponse getMyInfo(Long userId);
}
