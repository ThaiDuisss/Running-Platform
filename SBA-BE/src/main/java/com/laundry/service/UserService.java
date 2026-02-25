package com.laundry.service;

import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.UserResponse;

public interface UserService  {
    UserResponse register(UserRequest userRegister);
    UserResponse getMyInfo(String username);
}
