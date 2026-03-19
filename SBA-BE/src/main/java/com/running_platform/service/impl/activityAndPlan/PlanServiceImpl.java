package com.running_platform.service.impl.activityAndPlan;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.PlanMapper;
import com.running_platform.repository.PlanRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlanServiceImpl implements PlanService {

    PlanRepository planRepository;
    PlanMapper planMapper;
    UserRepository userRepository;

    @Override
    public ApiResponse<PlanResponse> create(PlanRequest request) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.NOT_FOUND));

        UserPlanWorkout plan = planMapper.toEntity(request);

        plan.setCreatedBy(user);

        try {
            UserPlanWorkout saved = planRepository.save(plan);

            return ApiResponse.<PlanResponse>builder()
                    .data(planMapper.EntityToRespond(saved))
                    .message("Create plan successfully")
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorEnum.UNKNOWN_ERROR);
        }
    }

    @Override
    public ApiResponse<PlanResponse> update(PlanRequest request, Long id) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        UserPlanWorkout plan = planRepository
                .findByIdAndCreatedBy_Id(id, userId)
                .orElseThrow(() -> new AppException(ErrorEnum.NOT_FOUND));

        planMapper.updateEntity(plan, request);

        UserPlanWorkout updated = planRepository.save(plan);

        return ApiResponse.<PlanResponse>builder()
                .data(planMapper.EntityToRespond(updated))
                .message("Update plan successfully")
                .build();
    }

    @Override
    public ApiResponse<PlanResponse> findPlanById(Long id) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        UserPlanWorkout plan = planRepository
                .findByIdAndCreatedBy_Id(id, userId)
                .orElseThrow(() -> new AppException(ErrorEnum.NOT_FOUND));

        return ApiResponse.<PlanResponse>builder()
                .data(planMapper.EntityToRespond(plan))
                .message("Get plan successfully")
                .build();
    }

    @Override
    public ApiResponse<Long> deleteById(Long id) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        UserPlanWorkout plan = planRepository
                .findByIdAndCreatedBy_Id(id, userId)
                .orElseThrow(() -> new AppException(ErrorEnum.NOT_FOUND));

        planRepository.delete(plan);

        return ApiResponse.<Long>builder()
                .data(id)
                .message("Delete plan successfully")
                .build();
    }

    @Override
    public ApiResponse<List<PlanResponse>> getByMonth(int year, int month) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<UserPlanWorkout> plans =
                planRepository.findByScheduledDateBetweenAndCreatedBy_Id(start, end, userId);

        return ApiResponse.<List<PlanResponse>>builder()
                .data(planMapper.toListRes(plans))
                .build();
    }
}