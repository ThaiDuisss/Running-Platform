package com.running_platform.controller;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{userId}")
    public ApiResponse<Void> sendFriendRequest(
            @PathVariable Long userId
    ) {

        friendService.sendRequest(userId);

        return ApiResponse.success(
                "Friend request sent",
                null
        );
    }

    @PutMapping("/accept/{id}")
    public ApiResponse<Void> acceptFriendRequest(
            @PathVariable Long id
    ) {

        friendService.acceptRequest(id);

        return ApiResponse.success(
                "Friend request accepted",
                null
        );
    }

}