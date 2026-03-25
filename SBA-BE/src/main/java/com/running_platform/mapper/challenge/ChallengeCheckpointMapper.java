package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeCheckpointRequest;
import com.running_platform.dto.response.challenge.ChallengeCheckpointResponse;
import com.running_platform.entity.RouteChallege.ChallengeCheckpoint;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeCheckpointMapper extends BaseMapper<
        ChallengeCheckpoint,
        AdminCreateChallengeCheckpointRequest,
        ChallengeCheckpointResponse
        > {
}
