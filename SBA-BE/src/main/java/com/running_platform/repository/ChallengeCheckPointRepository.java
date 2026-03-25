package com.running_platform.repository;

import com.running_platform.entity.RouteChallege.ChallengeCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeCheckPointRepository
        extends JpaRepository<ChallengeCheckpoint, Long> {
}
