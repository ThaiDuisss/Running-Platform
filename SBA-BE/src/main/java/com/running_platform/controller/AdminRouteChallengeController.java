package com.running_platform.controller;

import com.running_platform.dto.request.AdminCreateRouteChallengeRequest;
import com.running_platform.dto.request.AdminUpdateRouteChallengeRequest;
import com.running_platform.dto.response.AdminRouteChallengeResponse;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.service.RouteChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/route-challenges")
public class AdminRouteChallengeController {

    private final RouteChallengeService routeChallengeService;

    @PostMapping
    public ApiResponse<AdminRouteChallengeResponse> createRouteChallenge(
            @Valid @RequestBody AdminCreateRouteChallengeRequest request
    ) {
        return ApiResponse.created(
                "Route challenge created successfully",
                routeChallengeService.createRouteChallenge(request)
        );
    }

    @GetMapping
    public ApiResponse<PageResponse<AdminRouteChallengeResponse>> getRouteChallenges(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ApiResponse.success(
                "Route challenges retrieved successfully",
                routeChallengeService.getRouteChallenges(page, size)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminRouteChallengeResponse> getRouteChallengeById(
            @PathVariable Long id
    ) {

        return ApiResponse.success(
                "Route challenge retrieved successfully",
                routeChallengeService.getRouteChallengeById(id)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<AdminRouteChallengeResponse> updateRouteChallenge(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateRouteChallengeRequest request
    ) {

        return ApiResponse.success(
                "Route challenge updated successfully",
                routeChallengeService.updateRouteChallenge(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRouteChallenge(
            @PathVariable Long id
    ) {
        routeChallengeService.deleteRouteChallenge(id);
        return ApiResponse.success(
                "Deleted successfully",
                null);
    }
}