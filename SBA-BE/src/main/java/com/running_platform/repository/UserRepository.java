package com.running_platform.repository;

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
}
