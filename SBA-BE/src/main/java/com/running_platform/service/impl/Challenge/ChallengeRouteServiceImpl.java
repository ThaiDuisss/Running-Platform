package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRouteRequest;
import com.running_platform.dto.request.challenge.AdminUpdateChallengeRouteRequest;
import com.running_platform.dto.response.challenge.ChallengeRouteResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.RouteChallege.ChallengeCheckpoint;
import com.running_platform.entity.RouteChallege.ChallengeRoute;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeCheckpointMapper;
import com.running_platform.mapper.challenge.ChallengeRouteMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.ChallengeRouteRepository;
import com.running_platform.service.Challenge.ChallengeRouteService;
import com.running_platform.util.DistanceUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeRouteServiceImpl implements ChallengeRouteService {

    ChallengeRouteRepository challengeRouteRepository;
    ChallengeRepository challengeRepository;
    ChallengeRouteMapper challengeRouteMapper;
    ChallengeCheckpointMapper challengeCheckpointMapper;

    @Override
    @Transactional
    public ChallengeRouteResponse create(
            Long challengeId,
            AdminCreateChallengeRouteRequest request
    ) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() != ChallengeStatus.DRAFT) {
            throw new AppException(ErrorEnum.INVALID_REQUEST);
        }

        if (challengeRouteRepository.existsByChallenge_Id(challengeId)) {
            throw new AppException(ErrorEnum.CHALLENGE_ROUTE_ALREADY_EXISTS);
        }

        ChallengeRoute challengeRoute = challengeRouteMapper.toEntity(request);
        challengeRoute.setChallenge(challenge);

        String polyline = request.getPolyline();
        Double totalDistance = DistanceUtils.calculateDistanceFromPolyline(polyline);
        challengeRoute.setTotalDistance(totalDistance);

        challengeRoute.getCheckpoints().clear();

        challengeRoute.getCheckpoints().addAll(
                request.getCheckpoints().stream()
                        .map(cp -> {
                            ChallengeCheckpoint entity = challengeCheckpointMapper.toEntity(cp);
                            entity.setRoute(challengeRoute);
                            return entity;
                        })
                        .toList()
        );

        return challengeRouteMapper.EntityToRespond(
                challengeRouteRepository.save(challengeRoute)
        );
    }

    @Override
    @Transactional
    public ChallengeRouteResponse update(
            Long challengeId,
            Long routeId,
            AdminUpdateChallengeRouteRequest request
    ) {

        ChallengeRoute route = challengeRouteRepository.findById(routeId)
                .orElseThrow(() -> new AppException(ErrorEnum.ROUTE_NOT_FOUND));

        if (!route.getChallenge().getId().equals(challengeId)) {
            throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        challengeRouteMapper.update(route, request);

        route.getCheckpoints().clear();

        route.getCheckpoints().addAll(
                request.getCheckpoints().stream()
                        .map(cp -> {
                            ChallengeCheckpoint entity = challengeCheckpointMapper.toEntity(cp);
                            entity.setRoute(route);
                            return entity;
                        })
                        .toList()
        );

        return challengeRouteMapper.EntityToRespond(
                challengeRouteRepository.save(route)
        );
    }
}
