package com.running_platform.repository;

import com.running_platform.entity.UserAuth.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Users, Long> {
Optional<Users> findByUsername(String username);
boolean existsByUsername(String username);
}
