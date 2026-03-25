package com.running_platform.controller.ActivityAndPlan;

import com.running_platform.dto.request.activityAndPlan.PlanCustomRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.service.PlanTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan/custom")
@RequiredArgsConstructor
@Slf4j(topic = "CUSTOMPLAN")
public class CustomPlanController {
    private  final PlanTemplateService planTemplateService;
    @PostMapping
    public ApiResponse<Boolean> createPlan(@RequestBody PlanCustomRequest request){
        return planTemplateService.createPlanCustom(request);
    }
}
