package com.running_platform.controller.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRuleRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.challenge.ChallengeRuleResponse;
import com.running_platform.service.Challenge.ChallengeRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
public class ChallengeRuleController {

    private final ChallengeRuleService challengeRuleService;

    @PostMapping("/{challengeId}/rules")
    public ApiResponse<ChallengeRuleResponse> create(
            @PathVariable Long challengeId,
            @Valid @RequestBody AdminCreateChallengeRuleRequest request
    ) {
        return ApiResponse.created(
                "Challenge rule created successfully",
                challengeRuleService.createChallengeRule(
                        challengeId,
                        request
                ));
    }
}
