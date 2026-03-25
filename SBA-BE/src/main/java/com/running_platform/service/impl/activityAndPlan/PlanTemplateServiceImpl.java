package com.running_platform.service.impl.activityAndPlan;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.activityAndPlan.PlanCustomRequest;
import com.running_platform.dto.response.ApiResponse;
import com.running_platform.entity.RunActivities.Goals;
import com.running_platform.entity.RunActivities.PlanTemplates;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import com.running_platform.entity.RunActivities.UserPlans;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.VisibilityEnum;
import com.running_platform.exception.AppException;
import com.running_platform.repository.GoalRepository;
import com.running_platform.repository.PlanTemplateRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.PlanService;
import com.running_platform.service.PlanTemplateService;
import com.running_platform.service.UserPlanService;
import com.running_platform.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "PlanTemplateServiceImpl")
public class PlanTemplateServiceImpl implements PlanTemplateService {
    PlanTemplateRepository repository;
    GoalRepository goalRepository;
    UserPlanService userPlanService;
    ActivityServiceImpl activityService;
     UserRepository userRepository;
    private final PlanService planService;

    @Override
    @Transactional
    public ApiResponse<Boolean> createPlanCustom(PlanCustomRequest request) {
        try {
            Users u = userRepository.findById(AppSecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR))).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
            Goals g = goalRepository.findById(request.getGoal()).orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
            PlanTemplates planTemplates = PlanTemplates.builder().goal(g).name(request.getTitle()).level(request.getSelectedLevel())
                    .description(request.getTitle()).durationWeeks(request.getDurationWeeks())
                    .params(request.getParams()).visibilityEnum(VisibilityEnum.PUBLIC).build();
            planTemplates = repository.save(planTemplates);
             UserPlans userPlans = userPlanService.createPlanUserCustom(request, planTemplates);
            List<UserPlanWorkout> userPlanWorkoutList = request.getSchedule().stream().map(w ->{
                UserPlanWorkout userPlanWorkout = new UserPlanWorkout();
               userPlanWorkout.setUserPlan(userPlans);
               userPlanWorkout.setCreatedBy(u);
               userPlanWorkout.setDuration(w.getDuration());
               userPlanWorkout.setScheduledDate(w.getDate());
               userPlanWorkout.setTargetDistance(w.getTargetDistance());
               userPlanWorkout.setTitle(request.getTitle() + "("+ g.getTitle() + ")");
               return userPlanWorkout;
            }).toList();
            planService.saveAll(userPlanWorkoutList);
        }catch (RuntimeException e) {
            log.error("Create Plan False {}", e.toString());
            return ApiResponse.<Boolean>builder().data(false).code(510).build();
        }

        return ApiResponse.<Boolean>builder().data(true).code(200).message("create successfull").build();
    }
}
