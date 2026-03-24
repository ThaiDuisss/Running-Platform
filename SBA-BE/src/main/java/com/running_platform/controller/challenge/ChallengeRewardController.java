package com.running_platform.controller.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRewardRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.challenge.ChallengeRewardResponse;
import com.running_platform.service.Challenge.ChallengeRewardService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeRewardController {

    ChallengeRewardService challengeRewardService;

    @PutMapping("/{challengeId}/reward-challenges")
    public ApiResponse<ChallengeRewardResponse> create(
            @PathVariable Long challengeId,
            AdminCreateChallengeRewardRequest request
    ) {
        return ApiResponse.created(
                "Challenge created successfully",
                challengeRewardService.createChallengeReward(
                        challengeId,
                        request
                )
        );
    }

}
