package com.running_platform.controller.ActivityAndPlan;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.GoalResponse;
import com.running_platform.entity.RunActivities.Goals;
import com.running_platform.service.impl.activityAndPlan.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalsService;

    @GetMapping
    public ApiResponse<List<GoalResponse>> getGoals() {
        return goalsService.getAllActiveGoals();
    }
}
