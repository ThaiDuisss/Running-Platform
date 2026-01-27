package com.laundry.controller;


import com.laundry.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl service;
//
//    @GetMapping("/get-info/{userId}")
//    ApiResponse<ProfileResponse> getMyInfo(@PathVariable() Long userId ) {
//        return ApiResponse.<ProfileResponse>builder()
//                .code(200)
//                .status("Get Information Successful")
//                .data(service.getMyInfo(userId))
//                .build();
//    }
//
//    @GetMapping("/my-info")
//    ApiResponse<ProfileResponse> getMyInfo() {
//
//        return ApiResponse.<ProfileResponse>builder()
//                .data(service.getMyInfo(null))
//                .code(200)
//                .status("Get Information Successful")
//                .build();
//    }
}
