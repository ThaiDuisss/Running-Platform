package com.running_platform.service.Challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeCheckpointRequest;
import com.running_platform.dto.response.challenge.ChallengeCheckpointResponse;

import java.util.List;

public interface ChallengeCheckpointService {
    List<ChallengeCheckpointResponse> createChallengeCheckpoint(
            Long challengeId,
            List<AdminCreateChallengeCheckpointRequest> request
    );
}
