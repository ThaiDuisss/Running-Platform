package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.response.FriendResponse;
import com.running_platform.dto.response.FriendRequestResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.FriendStatus;
import com.running_platform.exception.AppException;
import com.running_platform.repository.FriendRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findIdByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
    }

    @Override
    public void sendRequest(Long userId) {
        Long currentUserId = getCurrentUserId();

        if (currentUserId.equals(userId)) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        if (friendRepository.existsByRequester_IdAndAddressee_Id(currentUserId, userId) ||
            friendRepository.existsByRequester_IdAndAddressee_Id(userId, currentUserId)) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        Users requester = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        Users addressee = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        FriendShips friend = FriendShips.builder()
                .requester(requester)
                .addressee(addressee)
                .status(FriendStatus.PENDING)
                .build();

        friendRepository.save(friend);
    }

    @Override
    public void acceptRequest(Long requestId) {
        Long currentUserId = getCurrentUserId();

        FriendShips friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        if (!friend.getAddressee().getId().equals(currentUserId)) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        friend.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friend);
    }

    @Override
    public void rejectRequest(Long requestId) {
        Long currentUserId = getCurrentUserId();

        FriendShips friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        if (!friend.getAddressee().getId().equals(currentUserId)) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        friendRepository.delete(friend);
    }

    @Override
    public void cancelRequest(Long userId) {
        Long currentUserId = getCurrentUserId();

        FriendShips friend = friendRepository.findByRequester_IdAndAddressee_Id(currentUserId, userId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        if (friend.getStatus() != FriendStatus.PENDING) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        friendRepository.delete(friend);
    }

    @Override
    public void unfriend(Long userId) {
        Long currentUserId = getCurrentUserId();

        FriendShips friend = friendRepository.findByRequester_IdAndAddressee_Id(currentUserId, userId)
                .orElse(friendRepository.findByRequester_IdAndAddressee_Id(userId, currentUserId)
                        .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR)));

        if (friend.getStatus() != FriendStatus.ACCEPTED) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        friendRepository.delete(friend);
    }

    @Override
    public PageResponse<FriendResponse> getFriends(int page, int size) {
        Long currentUserId = getCurrentUserId();

        Page<FriendShips> friends = friendRepository
                .findByStatusAndRequester_IdOrAddressee_Id(FriendStatus.ACCEPTED, currentUserId, currentUserId,
                        PageRequest.of(page, size));

        Page<FriendResponse> responsePage = friends.map(f -> {
            var otherUser = f.getRequester().getId().equals(currentUserId) ? f.getAddressee() : f.getRequester();
            return FriendResponse.builder()
                    .id(otherUser.getId())
                    .username(otherUser.getUsername())
                    .fullName(otherUser.getFullName())
                    .imageUrl(otherUser.getImageUrl())
                    .build();
        });

        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<FriendRequestResponse> getSentRequests(int page, int size) {
        Long currentUserId = getCurrentUserId();

        Page<FriendShips> requests = friendRepository.findByStatusAndRequester_Id(
                FriendStatus.PENDING,
                currentUserId,
                PageRequest.of(page, size)
        );

        Page<FriendRequestResponse> responsePage = requests.map(f -> FriendRequestResponse.builder()
                .id(f.getId())
                .userId(f.getAddressee().getId())
                .username(f.getAddressee().getUsername())
                .fullName(f.getAddressee().getFullName())
                .imageUrl(f.getAddressee().getImageUrl())
                .build());

        return PageResponse.from(responsePage);
    }

    @Override
    public PageResponse<FriendRequestResponse> getReceivedRequests(int page, int size) {
        Long currentUserId = getCurrentUserId();

        Page<FriendShips> requests = friendRepository.findByStatusAndAddressee_Id(
                FriendStatus.PENDING,
                currentUserId,
                PageRequest.of(page, size)
        );

        Page<FriendRequestResponse> responsePage = requests.map(f -> FriendRequestResponse.builder()
                .id(f.getId())
                .userId(f.getRequester().getId())
                .username(f.getRequester().getUsername())
                .fullName(f.getRequester().getFullName())
                .imageUrl(f.getRequester().getImageUrl())
                .build());

        return PageResponse.from(responsePage);
    }
}
