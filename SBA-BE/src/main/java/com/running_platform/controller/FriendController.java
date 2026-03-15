package com.running_platform.controller;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.FriendRequestResponse;
import com.running_platform.dto.response.FriendResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request/{addresseeId}")
    public ApiResponse<Void> sendFriendRequest(
            @PathVariable("addresseeId") Long addresseeId
    ) {

        friendService.sendRequest(addresseeId);

        return ApiResponse.success(
                "Friend request sent",
                null
        );
    }

    @PutMapping("/request/{requestId}/accept")
    public ApiResponse<Void> acceptFriendRequest(
            @PathVariable("requestId") Long requestId
    ) {

        friendService.acceptRequest(requestId);

        return ApiResponse.success(
                "Friend request accepted",
                null
        );
    }

    @PutMapping("/request/{requestId}/reject")
    public ApiResponse<Void> rejectFriendRequest(
            @PathVariable("requestId") Long requestId
    ) {

        friendService.rejectRequest(requestId);

        return ApiResponse.success(
                "Friend request rejected",
                null
        );
    }

    @DeleteMapping("/request/{addresseeId}")
    public ApiResponse<Void> cancelFriendRequest(
            @PathVariable("addresseeId") Long addresseeId
    ) {

        friendService.cancelRequest(addresseeId);

        return ApiResponse.success(
                "Friend request cancelled",
                null
        );
    }

    @DeleteMapping("/unfriend/{friendUserId}")
    public ApiResponse<Void> unfriend(
            @PathVariable("friendUserId") Long friendUserId
    ) {

        friendService.unfriend(friendUserId);

        return ApiResponse.success(
                "Unfriended",
                null
        );
    }

    @GetMapping
    public ApiResponse<PageResponse<FriendResponse>> getFriends(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<FriendResponse> friends = friendService.getFriends(page, size);

        return ApiResponse.success(
                "Friends retrieved",
                friends
        );
    }

    @GetMapping("/requests/sent")
    public ApiResponse<PageResponse<FriendRequestResponse>> getSentRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<FriendRequestResponse> requests = friendService.getSentRequests(page, size);

        return ApiResponse.success(
                "Sent requests retrieved",
                requests
        );
    }

    @GetMapping("/requests/received")
    public ApiResponse<PageResponse<FriendRequestResponse>> getReceivedRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<FriendRequestResponse> requests = friendService.getReceivedRequests(page, size);

        return ApiResponse.success(
                "Received requests retrieved",
                requests
        );
    }

}