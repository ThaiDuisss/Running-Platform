package com.laundry.controller;

import com.laundry.dto.request.UserRequest;
import com.laundry.dto.response.ApiResponse;
import com.laundry.dto.response.ProfileResponse;
import com.laundry.dto.response.UserResponse;
import com.laundry.entity.Users;
import com.laundry.enums.CustomHeader;
import com.laundry.service.UserService;
import com.laundry.utils.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController extends BaseController<Users, Long, UserRequest, UserResponse, UserService> {
    public UserController(UserService service) {
        super(service);
    }
    @GetMapping("/get-info/{userId}")
    ApiResponse<ProfileResponse> getMyInfo(@PathVariable("userId") Long userId ) {
        return ApiResponse.<ProfileResponse>builder()
                .code(200)
                .status("Get Information Successful")
                .data(service.getMyInfo(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<ProfileResponse> getMyInfo() {

        return ApiResponse.<ProfileResponse>builder()
                .data(service.getMyInfo(null))
                .code(200)
                .status("Get Information Successful")
                .build();
    }
}
