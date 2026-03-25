package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.request.challenge.AdminUpdateChallengeRouteRequest;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;
import com.running_platform.entity.RouteChallege.ChallengeRoute;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ChallengeRouteMapper extends BaseMapper<
        ChallengeRoute,
        AdminCreateChallengeRouteRequest,
        ChallengeRouteResponse
        > {

    void update(@MappingTarget ChallengeRoute entity, AdminUpdateChallengeRouteRequest request);
}
