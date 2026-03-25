package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.ChallengeRuleRequest;
import com.running_platform.dto.response.challenge.ChallengeRuleResponse;
import com.running_platform.entity.RouteChallege.ChallengeRule;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeRuleMapper extends BaseMapper<
        ChallengeRule,
        ChallengeRuleRequest,
        ChallengeRuleResponse> {

}
