package com.running_platform.service.impl;

import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.repository.FriendRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public void sendRequest(Long userId) {
//
////        Long currentUserId = SecurityUtil.getCurrentUserId();
//        long currentUserId = 1L; // Placeholder for current user ID
//        if (currentUserId.equals(userId)) {
//            throw new RuntimeException("Cannot add yourself");
//        }
//
//        if (friendRepository.existsByRequesterIdAndAddresseeId(currentUserId, userId)) {
//            throw new RuntimeException("Friend request already exists");
//        }
//
//        Users requester = userRepository.findById(currentUserId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Users addressee = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        FriendShips friend = FriendShips.builder()
//                .requester(requester)
//                .addressee(addressee)
//                .status(FriendStatus.PENDING)
//                .build();
//
//        friendRepository.save(friend);
    }

    @Override
    public void acceptRequest(Long requestId) {
//
//        FriendShips friend = friendRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Friend request not found"));
//
//        friend.setStatus(FriendStatus.ACCEPTED);
//
//        friendRepository.save(friend);
    }

}
