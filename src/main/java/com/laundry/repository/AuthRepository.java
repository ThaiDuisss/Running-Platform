package com.laundry.repository;

import com.laundry.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends BaseRepository<Users, Long> {
Optional<Users> findByUsername(String username);
boolean existsByUsername(String username);
}
