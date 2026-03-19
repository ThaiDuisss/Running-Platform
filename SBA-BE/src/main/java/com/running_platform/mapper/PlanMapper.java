package com.running_platform.mapper;

import com.running_platform.dto.request.activityAndPlan.PlanRequest;
import com.running_platform.dto.response.activityAndPlan.PlanResponse;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    UserPlanWorkout toEntity(PlanRequest request);

    PlanResponse EntityToRespond(UserPlanWorkout entity);

    List<PlanResponse> toListRes(List<UserPlanWorkout> list);

    void updateEntity(@MappingTarget UserPlanWorkout entity, PlanRequest request);
}