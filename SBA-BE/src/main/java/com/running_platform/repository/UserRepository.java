package com.running_platform.repository;

import com.running_platform.entity.UserAuth.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    boolean existsByUsername(String username);

    Page<Users> findByUsernameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}