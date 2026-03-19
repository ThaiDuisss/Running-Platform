package com.running_platform.controller.ActivityAndPlan;

import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;
import com.running_platform.service.PlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanController {
    PlanService planService;
    @GetMapping()
    public ApiResponse<List<PlanResponse>> getPlansByMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return planService.getByMonth(year, month);
    }
    @PostMapping
    public ApiResponse<PlanResponse> createPlan(
            @RequestBody PlanRequest request
    ) {
        return planService.create(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<PlanResponse> updatePlan(
            @PathVariable Long id,
            @RequestBody PlanRequest request
    ) {
        return planService.update(request, id);
    }
    @GetMapping("/{id}")
    public ApiResponse<PlanResponse> getById(@PathVariable Long id) {
        return planService.findPlanById(id);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Long> delete(@PathVariable Long id) {
        return planService.deleteById(id);
    }
}
