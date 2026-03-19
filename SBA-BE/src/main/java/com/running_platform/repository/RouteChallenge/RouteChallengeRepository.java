package com.running_platform.repository.RouteChallenge;

import com.running_platform.entity.RouteChallege.RouteChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteChallengeRepository
        extends JpaRepository<RouteChallenge, Long>,
        RouteChallengeRepositoryCustom {
}
