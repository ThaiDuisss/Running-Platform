package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.response.FollowNetworkResponse;
import com.running_platform.dto.response.FriendResponse;
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
    public FollowNetworkResponse getFollowNetwork(String tab, String keyword, Double radiusKm, int page, int size) {
        Long currentUserId = getCurrentUserId();
        Users currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        List<FriendShips> following = friendRepository.findByRequester_IdAndStatus(currentUserId, FriendStatus.ACCEPTED);
        List<FriendShips> followers = friendRepository.findByAddressee_IdAndStatus(currentUserId, FriendStatus.ACCEPTED);

        Set<Long> followingIds = following.stream()
                .map(follow -> follow.getAddressee().getId())
                .collect(Collectors.toSet());
        Set<Long> followerIds = followers.stream()
                .map(follow -> follow.getRequester().getId())
                .collect(Collectors.toSet());

        List<FriendResponse> filteredUsers = userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .map(user -> toFollowUser(user, currentUser, followingIds, followerIds))
                .filter(user -> matchesTab(tab, user))
                .filter(user -> matchesKeyword(keyword, user))
                .filter(user -> matchesRadius(radiusKm, user))
                .sorted(buildComparator(keyword))
                .toList();

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filteredUsers.size());
        List<FriendResponse> pageContent = start >= filteredUsers.size()
                ? List.of()
                : filteredUsers.subList(start, end);
        Page<FriendResponse> responsePage = new PageImpl<>(pageContent, pageable, filteredUsers.size());

        long discoverCount = Math.max(userRepository.count() - 1, 0);

        return FollowNetworkResponse.builder()
                .page(PageResponse.from(responsePage))
                .discoverCount(discoverCount)
                .followingCount(followingIds.size())
                .followersCount(followerIds.size())
                .build();
    }

    private FriendResponse toFollowUser(Users user, Users currentUser, Set<Long> followingIds, Set<Long> followerIds) {
        boolean isFollowing = followingIds.contains(user.getId());
        boolean isFollower = followerIds.contains(user.getId());

        return FriendResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .imageUrl(user.getImageUrl())
                .location(user.getLocation())
                .headline(user.getLocation() != null && !user.getLocation().isBlank() ? user.getLocation() : "Runner near you")
                .distanceKm(calculateDistanceKm(currentUser.getLatitude(), currentUser.getLongitude(), user.getLatitude(), user.getLongitude()))
                .relation(resolveRelation(isFollowing, isFollower))
                .following(isFollowing)
                .follower(isFollower)
                .build();
    }

    private String resolveRelation(boolean isFollowing, boolean isFollower) {
        if (isFollowing && isFollower) return "mutual";
        if (isFollowing) return "following";
        if (isFollower) return "follower";
        return "discover";
    }

    private boolean matchesTab(String tab, FriendResponse user) {
        String normalizedTab = tab == null ? "discover" : tab.toLowerCase(Locale.ROOT);
        return switch (normalizedTab) {
            case "following" -> user.isFollowing();
            case "followers" -> user.isFollower();
            default -> true;
        };
    }

    private boolean matchesKeyword(String keyword, FriendResponse user) {
        if (keyword == null || keyword.isBlank()) return true;

        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT).trim();
        String normalizedFullName = user.getFullName() == null ? "" : user.getFullName().toLowerCase(Locale.ROOT);
        String normalizedPhone = user.getPhoneNumber() == null ? "" : user.getPhoneNumber().replaceAll("\\D", "");
        String keywordDigits = keyword.replaceAll("\\D", "");

        if (normalizedFullName.startsWith(normalizedKeyword) || normalizedFullName.contains(normalizedKeyword)) return true;
        if (!keywordDigits.isBlank() && normalizedPhone.contains(keywordDigits)) return true;

        return List.of(normalizedKeyword.split("\\s+")).stream()
                .filter(part -> !part.isBlank())
                .anyMatch(normalizedFullName::contains);
    }

    private boolean matchesRadius(Double radiusKm, FriendResponse user) {
        if (radiusKm == null) return true;
        return user.getDistanceKm() != null && user.getDistanceKm() <= radiusKm;
    }

    private Comparator<FriendResponse> buildComparator(String keyword) {
        return Comparator
                .comparingInt((FriendResponse user) -> searchScore(user, keyword)).reversed()
                .thenComparingInt((FriendResponse user) -> relationRank(user.getRelation())).reversed()
                .thenComparing(FriendResponse::getDistanceKm, Comparator.nullsLast(Double::compareTo));
    }

    private int searchScore(FriendResponse user, String keyword) {
        if (keyword == null || keyword.isBlank()) return 1;

        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT).trim();
        String keywordDigits = keyword.replaceAll("\\D", "");
        String fullName = user.getFullName() == null ? "" : user.getFullName().toLowerCase(Locale.ROOT);
        String phoneNumber = user.getPhoneNumber() == null ? "" : user.getPhoneNumber().replaceAll("\\D", "");

        if (fullName.startsWith(normalizedKeyword)) return 5;
        if (fullName.contains(normalizedKeyword)) return 4;
        if (!keywordDigits.isBlank() && phoneNumber.contains(keywordDigits)) return 5;

        return List.of(normalizedKeyword.split("\\s+")).stream()
                .filter(part -> !part.isBlank())
                .mapToInt(part -> fullName.contains(part) ? 2 : 0)
                .sum();
    }

    private int relationRank(String relation) {
        return switch (relation) {
            case "mutual" -> 4;
            case "following" -> 3;
            case "follower" -> 2;
            default -> 1;
        };
    }

    private Double calculateDistanceKm(String currentLatitude, String currentLongitude, String targetLatitude, String targetLongitude) {
        try {
            if (currentLatitude == null || currentLongitude == null || targetLatitude == null || targetLongitude == null) {
                return null;
            }

            double lat1 = Double.parseDouble(currentLatitude);
            double lon1 = Double.parseDouble(currentLongitude);
            double lat2 = Double.parseDouble(targetLatitude);
            double lon2 = Double.parseDouble(targetLongitude);

            double earthRadius = 6371.0;
            double deltaLat = Math.toRadians(lat2 - lat1);
            double deltaLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return Math.round(earthRadius * c * 10.0) / 10.0;
        } catch (Exception exception) {
            return null;
        }
    }
}
