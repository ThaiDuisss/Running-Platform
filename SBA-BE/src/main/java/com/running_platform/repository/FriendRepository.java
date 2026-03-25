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
                     FROM users u
                     JOIN users cu ON cu.id = :currentUserId
                     WHERE u.id <> :currentUserId
                       AND u.email_verified = true
                       AND EXISTS (
                           SELECT 1
                           FROM friend_ships f1
                           WHERE f1.requester_id = :currentUserId
                             AND f1.addressee_id = u.id
                             AND f1.status = 'ACCEPTED'
                       )
                       AND EXISTS (
                           SELECT 1
                           FROM friend_ships f2
                           WHERE f2.requester_id = u.id
                             AND f2.addressee_id = :currentUserId
                             AND f2.status = 'ACCEPTED'
                       )
                       AND (
                           :keyword IS NULL OR :keyword = ''
                           OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                           OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
                       )
                       AND (
                           :radiusKm IS NULL
                           OR ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000 <= :radiusKm
                       )
            """, nativeQuery = true)
    Long countFriends(@Param("currentUserId") Long currentUserId,
                      @Param("keyword") String keyword,
                      @Param("radiusKm") Double radiusKm);

    @Query(value = """
            SELECT COUNT(*)
                    FROM users u
                    JOIN friend_ships f ON f.addressee_id = u.id
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE f.requester_id = :currentUserId
                      AND u.email_verified = true
                      AND f.status = 'ACCEPTED'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM friend_ships f2
                          WHERE f2.requester_id = u.id
                            AND f2.addressee_id = :currentUserId
                            AND f2.status = 'ACCEPTED'
                      )
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000 <= :radiusKm
                      )
            """, nativeQuery = true)
    Long countOnlyFollowing(@Param("currentUserId") Long currentUserId,
                            @Param("keyword") String keyword,
                            @Param("radiusKm") Double radiusKm);

    @Query(value = """
            SELECT COUNT(*)
                    FROM users u
                    JOIN friend_ships f ON f.requester_id = u.id
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE f.addressee_id = :currentUserId
                      AND u.email_verified = true
                      AND f.status = 'ACCEPTED'
                      AND NOT EXISTS (
                          SELECT 1
                          FROM friend_ships f2
                          WHERE f2.requester_id = :currentUserId
                            AND f2.addressee_id = u.id
                            AND f2.status = 'ACCEPTED'
                      )
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000 <= :radiusKm
                      )
            """, nativeQuery = true)
    Long countOnlyFollower(@Param("currentUserId") Long currentUserId,
                           @Param("keyword") String keyword,
                           @Param("radiusKm") Double radiusKm);

    @Query(value = """
             SELECT COUNT(*)
                                FROM users u
                                JOIN users cu ON cu.id = :currentUserId
                                WHERE u.id <> :currentUserId
                                  AND u.email_verified = true
                                  AND NOT EXISTS (
                                      SELECT 1
                                      FROM friend_ships f1
                                      WHERE f1.requester_id = :currentUserId
                                        AND f1.addressee_id = u.id
                                        AND f1.status = 'ACCEPTED'
                                  )
                                  AND NOT EXISTS (
                                      SELECT 1
                                      FROM friend_ships f2
                                      WHERE f2.requester_id = u.id
                                        AND f2.addressee_id = :currentUserId
                                        AND f2.status = 'ACCEPTED'
                                  )
                                  AND (
                                      :keyword IS NULL OR :keyword = ''
                                      OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                      OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                      OR LOWER(u.phone_number) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                  )
                                  AND (
                                      :radiusKm IS NULL
                                      OR ST_Distance_Sphere(u.location_detail, cu.location_detail) / 1000 <= :radiusKm
                                  )
            """, nativeQuery = true)
    Long countDiscover(@Param("currentUserId") Long currentUserId,
                       @Param("keyword") String keyword,
                       @Param("radiusKm") Double radiusKm);
}
