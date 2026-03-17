package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.FriendShips;
import com.running_platform.enums.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository
        extends JpaRepository<FriendShips ,Long> {
    boolean existsByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    Optional<FriendShips> findByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    Page<FriendShips> findByStatusAndRequester_Id(FriendStatus status, Long requesterId, Pageable pageable);

    Page<FriendShips> findByStatusAndAddressee_Id(FriendStatus status, Long addresseeId, Pageable pageable);

    @Query("""
    SELECT f
    FROM FriendShips f
    WHERE f.status = :status
    AND (f.requester.id = :userId OR f.addressee.id = :userId)
    """)
    Page<FriendShips> findFriends(
            @Param("status") FriendStatus status,
            @Param("userId") Long userId,
            Pageable pageable
    );
}
