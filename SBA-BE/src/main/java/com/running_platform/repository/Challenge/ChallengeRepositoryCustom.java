package com.running_platform.repository.Challenge;

import com.running_platform.dto.request.challenge.FilterChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import org.springframework.data.domain.Page;

public interface ChallengeRepositoryCustom {
    Page<ChallengeResponse> filter(FilterChallengeRequest request, int page, int size);
}
