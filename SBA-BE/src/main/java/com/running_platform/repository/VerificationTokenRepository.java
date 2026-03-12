package com.running_platform.repository;

import com.running_platform.entity.UserAuth.VerificationTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokens, Long> {
    VerificationTokens findByToken(String token);
}
