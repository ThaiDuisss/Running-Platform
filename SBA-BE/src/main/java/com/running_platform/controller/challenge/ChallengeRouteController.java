package com.running_platform.controller.challenge;


import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.request.challenge.AdminUpdateChallengeRouteRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;
import com.running_platform.service.Challenge.ChallengeRouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ChallengeRouteController {

    ChallengeRouteService challengeRouteService;

    @PostMapping("/{challengeId}/routes")
    public ApiResponse<ChallengeRouteResponse> create(
             @PathVariable Long challengeId,
             @RequestBody AdminCreateChallengeRouteRequest request
    ) {
        return ApiResponse.created(
                "Challenge created successfully",
                challengeRouteService.create(
                        challengeId,
                        request
                )
        );
    }

    @PutMapping("/{challengeId}/routes/{routeId}")
    public ApiResponse<ChallengeRouteResponse> update(
            @PathVariable Long challengeId,
            @PathVariable Long routeId,
            @RequestBody @Valid AdminUpdateChallengeRouteRequest request
    ) {
        return ApiResponse.success(
                "Challenge route updated successfully",
                challengeRouteService.update(
                        challengeId,
                        routeId,
                        request
                )
        );
    }
}
