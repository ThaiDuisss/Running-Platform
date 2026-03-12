package com.running_platform.repository;

import com.running_platform.entity.UserAuth.PasswordResetTokens;
import com.running_platform.entity.UserAuth.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokens, String> {
        Optional<PasswordResetTokens> findByToken(String token);
        void deleteByToken(String token);
       PasswordResetTokens findByUser(Users user);
}
