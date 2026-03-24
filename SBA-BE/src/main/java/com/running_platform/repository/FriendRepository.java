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
        extends JpaRepository<FriendShips, Long> {
    boolean existsByRequester_IdAndAddressee_Id(Long requesterId, Long addresseeId);

    boolean existsByRequester_IdAndAddressee_IdAndStatus(Long requesterId, Long addresseeId, FriendStatus status);

    Optional<FriendShips> findByRequester_IdAndAddressee_IdAndStatus(Long requesterId, Long addresseeId, FriendStatus status);

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

    @Query(value = """
            SELECT COUNT(*)
                FROM friend_ships f1
                JOIN friend_ships f2\s
                  ON f1.requester_id = f2.addressee_id\s
                 AND f1.addressee_id = f2.requester_id
                WHERE f1.requester_id = :currentUserId
                  AND f1.status = 'ACCEPTED'
                  AND f2.status = 'ACCEPTED'
            """, nativeQuery = true)
    Long countFriends(@Param("currentUserId") Long currentUserId);

    @Query(value = """
            SELECT COUNT(*)
            FROM friend_ships f
            WHERE f.requester_id = :currentUserId
              AND f.status = 'ACCEPTED'
              AND NOT EXISTS (
                  SELECT 1 FROM friend_ships f2
                  WHERE f2.requester_id = f.addressee_id
                    AND f2.addressee_id = :currentUserId
                    AND f2.status = 'ACCEPTED'
              )
            """, nativeQuery = true)
    Long countOnlyFollowing(@Param("currentUserId") Long currentUserId);

    @Query(value = """
            SELECT COUNT(*)
            FROM friend_ships f
            WHERE f.addressee_id = :currentUserId
              AND f.status = 'ACCEPTED'
              AND NOT EXISTS (
                  SELECT 1 FROM friend_ships f2
                  WHERE f2.requester_id = :currentUserId
                    AND f2.addressee_id = f.requester_id
                    AND f2.status = 'ACCEPTED'
              )
            """, nativeQuery = true)
    Long countOnlyFollower(@Param("currentUserId") Long currentUserId);

}
