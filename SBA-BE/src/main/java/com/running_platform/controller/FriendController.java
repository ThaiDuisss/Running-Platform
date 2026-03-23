package com.running_platform.controller;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.FollowNetworkResponse;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/network")
    public ApiResponse<FollowNetworkResponse> getFollowNetwork(
            @RequestParam(defaultValue = "discover") String tab,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.success(
                "Follow network retrieved",
                friendService.getFollowNetwork(tab, keyword, radiusKm, page, size)
        );
    }

    @PostMapping("/follow/{userId}")
    public ApiResponse<Void> follow(@PathVariable Long userId) {
        friendService.follow(userId);
        return ApiResponse.success("Followed user successfully", null);
    }

    @DeleteMapping("/follow/{userId}")
    public ApiResponse<Void> unfollow(@PathVariable Long userId) {
        friendService.unfollow(userId);
        return ApiResponse.success("Unfollowed user successfully", null);
    }
}
