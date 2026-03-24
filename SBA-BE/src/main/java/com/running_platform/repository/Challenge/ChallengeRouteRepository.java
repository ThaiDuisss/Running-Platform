package com.running_platform.repository.Challenge;

import com.running_platform.entity.RouteChallege.ChallengeRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeRouteRepository extends JpaRepository<ChallengeRoute, Long> {

    boolean existsByChallenge_Id(Long challengeId);

    Optional<ChallengeRoute> findByChallenge_Id(Long challengeId);

}
