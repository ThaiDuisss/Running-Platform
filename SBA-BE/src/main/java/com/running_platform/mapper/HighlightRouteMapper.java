package com.running_platform.mapper;

import com.running_platform.dto.request.HighlightRouteRequest;
import com.running_platform.dto.response.HighlightRouteResponse;
import com.running_platform.entity.RunActivities.HighlightRoute;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HighlightRouteMapper extends BaseMapper<
        HighlightRoute,
        HighlightRouteRequest,
        HighlightRouteResponse
        >{

    void update(@MappingTarget HighlightRoute entity, HighlightRouteRequest request);

}
