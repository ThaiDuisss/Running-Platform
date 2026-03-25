package com.running_platform.service;

import com.running_platform.dto.request.activityAndPlan.PlanCustomRequest;
import com.running_platform.dto.response.ApiResponse;

public interface PlanTemplateService {
    ApiResponse<Boolean> createPlanCustom(PlanCustomRequest request);

}

