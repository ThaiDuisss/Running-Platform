package com.running_platform.service;

import com.running_platform.dto.response.FriendResponse;
import com.running_platform.dto.response.FriendRequestResponse;
import com.running_platform.dto.response.PageResponse;


public interface FriendService {
    void sendRequest(Long userId);

    void acceptRequest(Long requestId);

    void rejectRequest(Long requestId);

    void cancelRequest(Long userId);

    void unfriend(Long userId);

    PageResponse<FriendResponse> getFriends(int page, int size);

    PageResponse<FriendRequestResponse> getSentRequests(int page, int size);

    PageResponse<FriendRequestResponse> getReceivedRequests(int page, int size);
}
