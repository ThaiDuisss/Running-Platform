package com.running_platform.mapper;

import com.running_platform.dto.request.AdminCreateRouteChallengeRequest;
import com.running_platform.dto.request.AdminUpdateRouteChallengeRequest;
import com.running_platform.dto.response.AdminRouteChallengeResponse;
import com.running_platform.entity.RouteChallege.RouteChallenge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RouteChallengeMapper {

    @Mapping(source = "type", target = "challengeType")
    RouteChallenge toEntity(AdminCreateRouteChallengeRequest request);

    @Mapping(source = "challengeType", target = "type")
    AdminRouteChallengeResponse toResponse(RouteChallenge entity);

    void updateEntity(
            AdminUpdateRouteChallengeRequest request,
            @MappingTarget RouteChallenge entity
    );
}
