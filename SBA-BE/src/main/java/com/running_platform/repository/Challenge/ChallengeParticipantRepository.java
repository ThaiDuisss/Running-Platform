package com.running_platform.repository.Challenge;

import com.running_platform.entity.RouteChallege.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {

}
