package com.running_platform.service.Challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;

public interface ChallengeRouteService {

    ChallengeRouteResponse createChallengeRoute(
            Long challengeId,
            AdminCreateChallengeRouteRequest request
    );

}
