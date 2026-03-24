package com.running_platform.repository.Challenge;

import com.running_platform.entity.RouteChallege.ChallengeReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRewardRepository
        extends JpaRepository<ChallengeReward, Long> {
    boolean existsByChallenge_Id(Long challengeId);
}
