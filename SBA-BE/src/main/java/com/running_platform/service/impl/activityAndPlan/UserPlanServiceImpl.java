package com.running_platform.service.impl.activityAndPlan;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.activityAndPlan.PlanCustomRequest;
import com.running_platform.entity.RunActivities.Goals;
import com.running_platform.entity.RunActivities.PlanTemplates;
import com.running_platform.entity.RunActivities.UserPlans;
import com.running_platform.enums.VisibilityEnum;
import com.running_platform.exception.AppException;
import com.running_platform.repository.UserPlanRepository;
import com.running_platform.service.UserPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "UserPlanServiceImpl")
public class UserPlanServiceImpl implements UserPlanService {
    UserPlanRepository repository;
    @Override
    public UserPlans createPlanUserCustom(PlanCustomRequest request, PlanTemplates planTemplates) {
        UserPlans userPlans = new UserPlans();
        try {
            LocalDate startDate = request.getDayStart().toLocalDate();
            LocalDate endDate = startDate.plusWeeks(request.getDurationWeeks());
             userPlans = UserPlans.builder().title(request.getTitle()).endDate(endDate).startDate(startDate)
                     .planTemplate(planTemplates).build();
        }catch (RuntimeException e) {
            log.error("Create Plan False {}", e.toString());
            return null;
        }
        return repository.save(userPlans);

    }
}
