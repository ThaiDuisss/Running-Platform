package com.running_platform.mapper;

import com.running_platform.dto.request.activityAndPlan.ActivityRequest;
import com.running_platform.dto.response.activityAndPlan.ActivityResponse;
import com.running_platform.entity.RunActivities.RunActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper extends BaseMapper<RunActivity, ActivityRequest, ActivityResponse> {
    RunActivity toEntity(ActivityRequest request);
}
