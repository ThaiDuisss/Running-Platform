package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.FriendShips;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository
        extends JpaRepository<FriendShips ,Long> {
    boolean existsByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    Optional<FriendShips> findByRequester_Id(Long requesterId);
}
