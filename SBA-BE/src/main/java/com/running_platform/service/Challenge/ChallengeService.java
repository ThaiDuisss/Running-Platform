package com.running_platform.service.Challenge;


import com.running_platform.dto.request.challenge.AdminUpdateChallengeRequest;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.request.challenge.FilterChallengeRequest;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.dto.response.challenge.ChallengeResponse;

public interface ChallengeService {

    ChallengeResponse createChallenge(
            AdminCreateChallengeRequest request
    );

    ChallengeResponse publish(Long id);

    PageResponse<ChallengeResponse> filter(
            FilterChallengeRequest request,
            int size,
            int page
    );

    void delete(Long id);

    ChallengeResponse update(
            Long id,
            AdminUpdateChallengeRequest request
    );

    ChallengeResponse getById(Long id);
}
