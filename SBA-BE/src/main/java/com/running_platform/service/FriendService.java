package com.running_platform.service;

public interface FriendService {
    void sendRequest(Long userId);

    void acceptRequest(Long requestId);
}
