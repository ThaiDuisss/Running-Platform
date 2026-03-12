package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.FriendStatus;
import com.running_platform.exception.AppException;
import com.running_platform.repository.FriendRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public void sendRequest(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = userRepository.findIdByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        //check if user is trying to send friend request to themselves
        if (currentUserId.equals(userId)) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }

        //check if friend request already exists
        if (friendRepository.existsByRequester_IdAndAddressee_Id(currentUserId, userId)) {
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

        FriendShips friend = friendRepository.findByRequester_Id(requestId)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        friend.setStatus(FriendStatus.ACCEPTED);

        friendRepository.save(friend);
    }

}
