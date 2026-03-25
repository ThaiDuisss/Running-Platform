package com.running_platform.controller.challenge;

import com.running_platform.dto.request.challenge.AdminUpdateChallengeRequest;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.request.challenge.FilterChallengeRequest;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.service.Challenge.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ApiResponse<ChallengeResponse> create(
            @Valid @RequestBody AdminCreateChallengeRequest request
    ) {
        return ApiResponse.created(
                "Challenge created successfully",
                challengeService.createChallenge(request)
        );
    }

    @PatchMapping("/{challengeId}/publish")
    public ApiResponse<ChallengeResponse> publish(
            @PathVariable Long challengeId
    ) {
        return ApiResponse.success(
                "Challenge published successfully",
                challengeService.publish(challengeId)
        );
    }

    @GetMapping("/filters")
    public ApiResponse<PageResponse<ChallengeResponse>> filter(
            @ModelAttribute FilterChallengeRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ApiResponse.success(
                "filter challenge successfully",
                challengeService.filter(request, page, size)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ChallengeResponse> updateRouteChallenge(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateChallengeRequest request
    ) {

        return ApiResponse.success(
                "Challenge updated successfully",
                challengeService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id
    ) {
        challengeService.delete(id);
        return ApiResponse.success(
                "Deleted successfully",
                null);
    }


    @GetMapping("/{id}")
    public ApiResponse<ChallengeResponse> getChallengeById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                "get detail challenge retrieved successfully",
                challengeService.getById(id)
        );
    }
}