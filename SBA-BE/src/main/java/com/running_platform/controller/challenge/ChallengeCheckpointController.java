package com.running_platform.controller.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeCheckpointRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.challenge.ChallengeCheckpointResponse;
import com.running_platform.service.Challenge.ChallengeCheckpointService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeCheckpointController {

    ChallengeCheckpointService challengeCheckpointService;

    @PostMapping("/{challengeId}/checkpoints")
    public ApiResponse<List<ChallengeCheckpointResponse>> createCheckpoints(
            @PathVariable Long challengeId,
            List<AdminCreateChallengeCheckpointRequest> request
    ) {
        return ApiResponse.created(
                "Challenge created successfully",
                challengeCheckpointService.createChallengeCheckpoint(
                        challengeId,
                        request
                )
        );
    }
}
