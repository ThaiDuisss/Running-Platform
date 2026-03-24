package com.running_platform.service;

import com.running_platform.dto.response.FollowNetworkResponse;

public interface FriendService {
    void follow(Long userId);

    void unfollow(Long userId);

    FollowNetworkResponse getFollowNetwork(String tab, String keyword, Double radiusKm, int page, int size);
}
