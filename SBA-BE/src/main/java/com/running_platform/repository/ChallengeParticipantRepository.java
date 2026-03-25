package com.running_platform.repository;

import com.running_platform.entity.RouteChallege.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

}
