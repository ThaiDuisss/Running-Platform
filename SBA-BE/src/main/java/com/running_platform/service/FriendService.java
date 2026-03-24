package com.running_platform.service;

import com.running_platform.dto.response.FollowNetworkResponse;
import com.running_platform.enums.TabEnum;

public interface FriendService {
    void follow(Long userId);

    void unfollow(Long userId);

    FollowNetworkResponse getFollowNetwork(TabEnum tab, String keyword, Double radiusKm, int page, int size);
}
