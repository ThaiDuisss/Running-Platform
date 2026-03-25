package com.running_platform.service.Challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.request.challenge.AdminUpdateChallengeRouteRequest;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;

public interface ChallengeRouteService {

    ChallengeRouteResponse create(
            Long challengeId,
            AdminCreateChallengeRouteRequest request
    );

    ChallengeRouteResponse update(
            Long challengeId,
            Long routeId,
            AdminUpdateChallengeRouteRequest request
    );

}
