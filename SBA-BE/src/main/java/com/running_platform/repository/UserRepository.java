package com.running_platform.repository;

import com.running_platform.dto.FollowUserProjection;
import com.running_platform.entity.UserAuth.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

    Page<Users> findByUsernameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    @Query("""
            SELECT u FROM Users u
            WHERE u.id <> :currentUserId
            AND (
                :keyword IS NULL OR :keyword = '' OR
                LOWER(COALESCE(u.fullName, '')) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(COALESCE(u.phoneNumber, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    Page<Users> searchFollowCandidates(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT u.id FROM Users u WHERE u.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);

    @Query(value = """
            SELECT 
                u.id AS id,
                u.username AS username,
                u.full_name AS fullName,
                u.image_url AS imageUrl,
                u.address AS address,
                ROUND(ST_Distance_Sphere(u.location, cu.location) / 1000, 1) AS distanceKm            FROM users u
             JOIN users cu ON cu.id = :currentUserId
             WHERE u.id <> :currentUserId
               AND EXISTS (
                   SELECT 1
                   FROM friendships f1
                   WHERE f1.requester_id = :currentUserId
                     AND f1.addressee_id = u.id
                     AND f1.status = 'ACCEPTED'
               )
               AND EXISTS (
                   SELECT 1
                   FROM friendships f2
                   WHERE f2.requester_id = u.id
                     AND f2.addressee_id = :currentUserId
                     AND f2.status = 'ACCEPTED'
               )
               AND (
                   :keyword IS NULL OR :keyword = ''
                   OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
               )
               AND (
                   :radiusKm IS NULL
                   OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
               )
             ORDER BY u.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE u.id <> :currentUserId
                      AND EXISTS (
                          SELECT 1
                          FROM friendships f1
                          WHERE f1.requester_id = :currentUserId
                            AND f1.addressee_id = u.id
                            AND f1.status = 'ACCEPTED'
                      )
                      AND EXISTS (
                          SELECT 1
                          FROM friendships f2
                          WHERE f2.requester_id = u.id
                            AND f2.addressee_id = :currentUserId
                            AND f2.status = 'ACCEPTED'
                      )
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
                      )
                    """,
            nativeQuery = true)
    Page<FollowUserProjection> findFriends(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable
    );

    @Query(value = """
            SELECT 
                u.id AS id,
                u.username AS username,
                u.full_name AS fullName,
                u.image_url AS imageUrl,
                u.address AS address,
                ROUND(ST_Distance_Sphere(u.location, cu.location) / 1000, 1) AS distanceKm  
            FROM users u
            JOIN friendships f ON f.addressee_id = u.id
            JOIN users cu ON cu.id = :currentUserId
            WHERE f.requester_id = :currentUserId
              AND f.status = 'ACCEPTED'
              AND (
                  :keyword IS NULL OR :keyword = ''
                  OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
              AND (
                  :radiusKm IS NULL
                  OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
              )
            ORDER BY u.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    JOIN friendships f ON f.addressee_id = u.id
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE f.requester_id = :currentUserId
                      AND f.status = 'ACCEPTED'
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
                      )
                    """,
            nativeQuery = true)
    Page<FollowUserProjection> findFollowing(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable
    );

    @Query(value = """
            SELECT 
                u.id AS id,
                u.username AS username,
                u.full_name AS fullName,
                u.image_url AS imageUrl,
                u.address AS address,
                ROUND(ST_Distance_Sphere(u.location, cu.location) / 1000, 1) AS distanceKm  
            FROM users u
            JOIN friendships f ON f.requester_id = u.id
            JOIN users cu ON cu.id = :currentUserId
            WHERE f.addressee_id = :currentUserId
              AND f.status = 'ACCEPTED'
              AND (
                  :keyword IS NULL OR :keyword = ''
                  OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
              AND (
                  :radiusKm IS NULL
                  OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
              )
            ORDER BY u.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    JOIN friendships f ON f.requester_id = u.id
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE f.addressee_id = :currentUserId
                      AND f.status = 'ACCEPTED'
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
                      )
                    """,
            nativeQuery = true)
    Page<FollowUserProjection> findFollowers(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable
    );

    @Query(value = """
            SELECT 
                u.id AS id,
                u.username AS username,
                u.full_name AS fullName,
                u.image_url AS imageUrl,
                u.address AS address,
                ROUND(ST_Distance_Sphere(u.location, cu.location) / 1000, 1) AS distanceKm  
            FROM users u
            JOIN users cu ON cu.id = :currentUserId
            WHERE u.id <> :currentUserId
              AND NOT EXISTS (
                  SELECT 1
                  FROM friendships f1
                  WHERE f1.requester_id = :currentUserId
                    AND f1.addressee_id = u.id
                    AND f1.status = 'ACCEPTED'
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM friendships f2
                  WHERE f2.requester_id = u.id
                    AND f2.addressee_id = :currentUserId
                    AND f2.status = 'ACCEPTED'
              )
              AND (
                  :keyword IS NULL OR :keyword = ''
                  OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
              AND (
                  :radiusKm IS NULL
                  OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
              )
            ORDER BY u.created_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM users u
                    JOIN users cu ON cu.id = :currentUserId
                    WHERE u.id <> :currentUserId
                      AND NOT EXISTS (
                          SELECT 1
                          FROM friendships f1
                          WHERE f1.requester_id = :currentUserId
                            AND f1.addressee_id = u.id
                            AND f1.status = 'ACCEPTED'
                      )
                      AND NOT EXISTS (
                          SELECT 1
                          FROM friendships f2
                          WHERE f2.requester_id = u.id
                            AND f2.addressee_id = :currentUserId
                            AND f2.status = 'ACCEPTED'
                      )
                      AND (
                          :keyword IS NULL OR :keyword = ''
                          OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.full_name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                          OR LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      )
                      AND (
                          :radiusKm IS NULL
                          OR ST_Distance_Sphere(u.location, cu.location) / 1000 <= :radiusKm
                      )
                    """,
            nativeQuery = true)
    Page<FollowUserProjection> findDiscoverUsers(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable
    );
}
