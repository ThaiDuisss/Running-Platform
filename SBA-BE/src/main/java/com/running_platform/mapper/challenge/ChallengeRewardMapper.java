package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRewardRequest;
import com.running_platform.dto.response.challenge.ChallengeRewardResponse;
import com.running_platform.entity.RouteChallege.ChallengeReward;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeRewardMapper extends BaseMapper<
        ChallengeReward,
        AdminCreateChallengeRewardRequest,
        ChallengeRewardResponse
        > {
}
