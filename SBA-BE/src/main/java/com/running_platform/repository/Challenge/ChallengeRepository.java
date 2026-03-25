package com.running_platform.repository.Challenge;

import com.running_platform.entity.RouteChallege.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends
        JpaRepository<Challenge, Long>,
        ChallengeRepositoryCustom {
    boolean existsByTitleAndCreatorId(String title, Long creatorId);
}
