package com.laundry.service;

import com.laundry.dto.request.UserRegister;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;

public interface UserService  {
    UserResponse register(UserRegister userRegister);
    ProfileResponse getMyInfo(Long userId);
}
