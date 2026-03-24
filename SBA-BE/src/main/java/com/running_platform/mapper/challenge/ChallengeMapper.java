package com.running_platform.mapper.challenge;

import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChallengeMapper extends BaseMapper<
                Challenge,
                AdminCreateChallengeRequest,
                ChallengeResponse
        > {

}
