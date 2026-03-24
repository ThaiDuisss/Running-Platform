package com.running_platform.controller.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
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

    @PatchMapping("/{challengeId}")
    public ApiResponse<ChallengeResponse> update(
            @PathVariable Long challengeId
    ) {
        return ApiResponse.success(
                "visibility updated successfully",
                challengeService.publish(challengeId)
        );
    }


//    @GetMapping
//    public ApiResponse<PageResponse<AdminRouteChallengeResponse>> getRouteChallenges(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//
//        return ApiResponse.success(
//                "Route challenges retrieved successfully",
//                routeChallengeService.getRouteChallenges(page, size)
//        );
//    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<AdminRouteChallengeResponse> getRouteChallengeById(
//            @PathVariable Long id
//    ) {
//
//        return ApiResponse.success(
//                "Route challenge retrieved successfully",
//                routeChallengeService.getRouteChallengeById(id)
//        );
//    }
//
//    @PutMapping("/{id}")
//    public ApiResponse<AdminRouteChallengeResponse> updateRouteChallenge(
//            @PathVariable Long id,
//            @Valid @RequestBody AdminUpdateRouteChallengeRequest request
//    ) {
//
//        return ApiResponse.success(
//                "Route challenge updated successfully",
//                routeChallengeService.updateRouteChallenge(id, request)
//        );
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteRouteChallenge(
//            @PathVariable Long id
//    ) {
//        routeChallengeService.deleteRouteChallenge(id);
//        return ApiResponse.success(
//                "Deleted successfully",
//                null);
//    }
}