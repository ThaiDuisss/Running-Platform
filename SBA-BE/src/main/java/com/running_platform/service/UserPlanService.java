package com.running_platform.service;

import com.running_platform.dto.request.activityAndPlan.PlanCustomRequest;
import com.running_platform.entity.RunActivities.PlanTemplates;
import com.running_platform.entity.RunActivities.UserPlans;

public interface UserPlanService {
    UserPlans createPlanUserCustom(PlanCustomRequest request, PlanTemplates planTemplates);
}
