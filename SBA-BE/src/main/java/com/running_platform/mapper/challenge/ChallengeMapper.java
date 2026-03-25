package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.AdminUpdateChallengeRequest;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ChallengeRuleMapper.class, ChallengeRewardMapper.class})
public interface ChallengeMapper extends BaseMapper<
                Challenge,
                AdminCreateChallengeRequest,
                ChallengeResponse
        > {
    @Mapping(target = "rule", source = "rule")
    @Mapping(target = "reward", source = "reward")
    ChallengeResponse EntityToRespond(Challenge e);

    void update(@MappingTarget Challenge entity, AdminUpdateChallengeRequest request);
}
