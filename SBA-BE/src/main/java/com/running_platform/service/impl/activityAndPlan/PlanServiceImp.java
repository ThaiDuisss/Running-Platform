package com.running_platform.service.impl.activityAndPlan;

import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import com.running_platform.mapper.PlanMapper;
import com.running_platform.repository.PlanRepository;
import com.running_platform.service.PlanService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanServiceImp implements PlanService {
    PlanRepository planRepository;
    PlanMapper planMapper;
    @Override
    public ApiResponse<PlanResponse> update(PlanRequest planRequest, Long id) {
        UserPlanWorkout plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        plan = planMapper.toEntity(planRequest);

        UserPlanWorkout updatedPlan = planRepository.save(plan);

        return ApiResponse.<PlanResponse>builder()
                .data(planMapper.EntityToRespond(updatedPlan))
                .message("Update plan successfully")
                .build();
    }

    @Override
    public ApiResponse<PlanResponse> create(PlanRequest planRequest) {

        UserPlanWorkout plan = planMapper.toEntity(planRequest);

        UserPlanWorkout savedPlan = planRepository.save(plan);

        PlanResponse response = planMapper.EntityToRespond(savedPlan);

        return ApiResponse.<PlanResponse>builder()
                .data(response)
                .message("Create plan successfully")
                .build();
    }

    @Override
    public ApiResponse<PlanResponse> findPlanById(Long id) {

        UserPlanWorkout plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        PlanResponse response = planMapper.EntityToRespond(plan);

        return ApiResponse.<PlanResponse>builder()
                .data(response)
                .message("Get plan successfully")
                .build();
    }

    @Override
    public ApiResponse<Long> deleteById(Long id) {

        UserPlanWorkout plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        planRepository.delete(plan);

        return ApiResponse.<Long>builder()
                .data(id)
                .message("Delete plan successfully")
                .build();
    }

    @Override
    public ApiResponse<List<PlanResponse>> getByMonth(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return ApiResponse.<List<PlanResponse>>builder().data(planMapper.toListRes(planRepository.findByScheduledDateBetween(start, end))).build();

    }
}
