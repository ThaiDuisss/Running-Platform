package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository
        extends JpaRepository<FriendShips ,Long> {
    boolean existsByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    Optional<FriendShips> findByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    Optional<FriendShips> findByRequester_Id(Long requesterId);

    List<FriendShips> findByStatusAndRequester_Id(FriendStatus status, Long requesterId);

    List<FriendShips> findByStatusAndAddressee_Id(FriendStatus status, Long addresseeId);

    List<FriendShips> findByStatusAndRequester_IdOrAddressee_Id(FriendStatus status, Long userId1, Long userId2);

    Page<FriendShips> findByStatusAndRequester_Id(FriendStatus status, Long requesterId, Pageable pageable);

    Page<FriendShips> findByStatusAndAddressee_Id(FriendStatus status, Long addresseeId, Pageable pageable);

    Page<FriendShips> findByStatusAndRequester_IdOrAddressee_Id(FriendStatus status, Long userId1, Long userId2, Pageable pageable);
}
