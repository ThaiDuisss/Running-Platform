package com.running_platform.service.Challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRewardRequest;
import com.running_platform.dto.response.challenge.ChallengeRewardResponse;

public interface ChallengeRewardService {

    ChallengeRewardResponse createChallengeReward(
            Long challengeId,
            AdminCreateChallengeRewardRequest request
    );

}
