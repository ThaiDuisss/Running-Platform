package com.running_platform.service.Challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRuleRequest;
import com.running_platform.dto.response.challenge.ChallengeRuleResponse;

public interface ChallengeRuleService {
    ChallengeRuleResponse createChallengeRule(
            Long challengeId,
            AdminCreateChallengeRuleRequest request
    );
}
