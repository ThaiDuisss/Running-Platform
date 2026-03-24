package com.running_platform.service.Challenge;


import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;

public interface ChallengeService {
    ChallengeResponse createChallenge(
            AdminCreateChallengeRequest request
    );

    ChallengeResponse publish(Long id);

//    PageResponse<AdminRouteChallengeResponse> getRouteChallenges(
//            int page,
//            int size
//    );
//
//    AdminRouteChallengeResponse getRouteChallengeById(Long id);
//
//    AdminRouteChallengeResponse updateRouteChallenge(
//            Long id,
//            AdminUpdateRouteChallengeRequest request
//    );
//
//    void deleteRouteChallenge(Long id);
}
