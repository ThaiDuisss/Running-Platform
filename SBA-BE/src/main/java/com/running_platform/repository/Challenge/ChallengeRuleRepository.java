package com.running_platform.repository.Challenge;

import com.running_platform.entity.RouteChallege.ChallengeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChallengeRuleRepository
        extends JpaRepository<ChallengeRule, Long> {

    Optional<ChallengeRule> findByChallenge_Id(Long challengeId);

    boolean existsByChallenge_Id(Long challengeId);
}
