package com.running_platform.service;


import com.running_platform.dto.request.AdminCreateRouteChallengeRequest;
import com.running_platform.dto.request.AdminUpdateRouteChallengeRequest;
import com.running_platform.dto.response.AdminRouteChallengeResponse;
import com.running_platform.dto.response.PageResponse;

public interface RouteChallengeService {
    AdminRouteChallengeResponse createRouteChallenge(
            AdminCreateRouteChallengeRequest request
    );

    PageResponse<AdminRouteChallengeResponse> getRouteChallenges(
            int page,
            int size
    );

    AdminRouteChallengeResponse getRouteChallengeById(Long id);

    AdminRouteChallengeResponse updateRouteChallenge(
            Long id,
            AdminUpdateRouteChallengeRequest request
    );

    void deleteRouteChallenge(Long id);
}
