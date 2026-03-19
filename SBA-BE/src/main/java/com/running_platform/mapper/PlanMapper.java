package com.running_platform.mapper;

import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper extends BaseMapper<UserPlanWorkout, PlanRequest, PlanResponse>{
}
