package com.running_platform.service;

import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;

import java.util.List;

public interface PlanService {
    ApiResponse<PlanResponse> update(PlanRequest planRequest, Long id);
    ApiResponse<PlanResponse> create(PlanRequest planRequest);
    ApiResponse<PlanResponse> findPlanById(Long Id);
    ApiResponse<Long> deleteById(Long Id);
    ApiResponse<List<PlanResponse>> getByMonth(int year, int month);
}
