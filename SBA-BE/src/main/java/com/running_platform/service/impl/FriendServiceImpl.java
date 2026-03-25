package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.FollowUserProjection;
import com.running_platform.dto.response.FollowNetworkResponse;
import com.running_platform.dto.response.FriendResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.FriendStatus;
import com.running_platform.enums.TabEnum;
import com.running_platform.exception.AppException;
import com.running_platform.repository.FriendRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void follow(Long userId) {
        Long currentUserId = getCurrentUserId();

        if (currentUserId.equals(userId)) {
            throw new AppException(ErrorEnum.SELF_FRIEND_REQUEST);
        }

        if (friendRepository.existsByRequester_IdAndAddressee_IdAndStatus(currentUserId, userId, FriendStatus.ACCEPTED)) {
            throw new AppException(ErrorEnum.EXISTING_FRIEND_REQUEST);
        }

        Users requester = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));
        Users addressee = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        FriendShips follow = FriendShips.builder()
                .requester(requester)
                .addressee(addressee)
                .status(FriendStatus.ACCEPTED)
                .build();

        friendRepository.save(follow);
    }

    @Override
    public void unfollow(Long userId) {
        Long currentUserId = getCurrentUserId();

        FriendShips follow = friendRepository.findByRequester_IdAndAddressee_IdAndStatus(currentUserId, userId, FriendStatus.ACCEPTED)
                .orElseThrow(() -> new AppException(ErrorEnum.FRIEND_SHIP_NOT_FOUND));

        friendRepository.delete(follow);
    }

    @Override
    public FollowNetworkResponse getFollowNetwork(TabEnum tab, String keyword, Double radiusKm, int page, int size) {
        Long currentUserId = getCurrentUserId();

        userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);
        String normalizedKeyword = keyword != null ? keyword.trim() : null;

        Page<FollowUserProjection> usersPage = switch (tab) {
            case FRIEND -> userRepository.findFriends(currentUserId, normalizedKeyword, radiusKm, pageable);
            case FOLLOWING -> userRepository.findFollowingOnly(currentUserId, normalizedKeyword, radiusKm, pageable);
            case FOLLOWERS -> userRepository.findFollowersOnly(currentUserId, normalizedKeyword, radiusKm, pageable);
            case DISCOVER -> userRepository.findDiscoverUsers(currentUserId, normalizedKeyword, radiusKm, pageable);
        };

        List<FriendResponse> content = usersPage.getContent().stream()
                .map(user -> toFriendResponse(user, tab))
                .toList();

        Page<FriendResponse> responsePage = new PageImpl<>(content, pageable, usersPage.getTotalElements());

        long followingCount = friendRepository.countOnlyFollowing(currentUserId, keyword, radiusKm);
        long followersCount = friendRepository.countOnlyFollower(currentUserId, keyword, radiusKm);
        long friendCount = friendRepository.countFriends(currentUserId,keyword, radiusKm);
        long discoverCount = friendRepository.countDiscover(currentUserId,keyword, radiusKm);

        return FollowNetworkResponse.builder()
                .page(PageResponse.from(responsePage))
                .discoverCount(discoverCount)
                .followingCount(followingCount)
                .followersCount(followersCount)
                .friendCount(friendCount)
                .build();
    }

    private FriendResponse toFriendResponse(FollowUserProjection user, TabEnum relation) {
        return FriendResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .imageUrl(user.getImageUrl())
                .location(user.getLocation())
                .distanceKm(user.getDistanceKm())
                .relation(relation)
                .build();
    }
}
