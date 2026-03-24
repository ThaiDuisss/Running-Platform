package com.running_platform.controller.challenge;


import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;
import com.running_platform.service.Challenge.ChallengeRouteService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ChallengeRouteController {

    ChallengeRouteService challengeRouteService;

    @PostMapping("/{challengeId}/routes")
    public ApiResponse<ChallengeRouteResponse> create(
             @PathVariable Long challengeId,
             AdminCreateChallengeRouteRequest request
    ) {
        return ApiResponse.created(
                "Challenge created successfully",
                challengeRouteService.createChallengeRoute(
                        challengeId,
                        request
                )
        );
    }
}
