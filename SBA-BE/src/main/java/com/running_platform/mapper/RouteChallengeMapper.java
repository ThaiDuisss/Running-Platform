package com.running_platform.mapper;

import com.running_platform.dto.request.AdminCreateRouteChallengeRequest;
import com.running_platform.dto.request.AdminUpdateRouteChallengeRequest;
import com.running_platform.dto.response.AdminRouteChallengeResponse;
import com.running_platform.entity.RouteChallege.RouteChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteChallengeMapper {

    RouteChallenge toEntity(AdminCreateRouteChallengeRequest request);

    AdminRouteChallengeResponse toResponse(RouteChallenge entity);

    void updateEntity(
            AdminUpdateRouteChallengeRequest request,
            @MappingTarget RouteChallenge entity
    );
}
