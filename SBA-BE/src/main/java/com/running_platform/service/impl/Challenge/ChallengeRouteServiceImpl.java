package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.RouteChallege.ChallengeRoute;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeRouteMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.Challenge.ChallengeRouteRepository;
import com.running_platform.service.Challenge.ChallengeRouteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeRouteServiceImpl implements ChallengeRouteService {

    ChallengeRouteRepository challengeRouteRepository;
    ChallengeRepository challengeRepository;
    ChallengeRouteMapper challengeRouteMapper;

    @Override
    public ChallengeRouteResponse createChallengeRoute(
            Long challengeId,
            AdminCreateChallengeRouteRequest request
    ) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() != ChallengeStatus.DRAFT) {
            throw new AppException(ErrorEnum.INVALID_REQUEST);
        }

        if(challengeRouteRepository.existsByChallenge_Id(challengeId)) {
            throw new AppException(ErrorEnum.CHALLENGE_ROUTE_ALREADY_EXISTS);
        }

        ChallengeRoute challengeRoute = challengeRouteMapper.toEntity(request);

        challengeRoute.setChallenge(challenge);

        ChallengeRoute saved = challengeRouteRepository.save(challengeRoute);

        return challengeRouteMapper.EntityToRespond(saved);
    }

}
