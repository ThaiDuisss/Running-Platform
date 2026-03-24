package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeCheckpointRequest;
import com.running_platform.dto.response.challenge.ChallengeCheckpointResponse;
import com.running_platform.entity.RouteChallege.ChallengeCheckpoint;
import com.running_platform.entity.RouteChallege.ChallengeRoute;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeCheckpointMapper;
import com.running_platform.repository.Challenge.ChallengeCheckPointRepository;
import com.running_platform.repository.Challenge.ChallengeRouteRepository;
import com.running_platform.service.Challenge.ChallengeCheckpointService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeCheckpointServiceImpl implements ChallengeCheckpointService {

    ChallengeRouteRepository challengeRouteRepository;
    ChallengeCheckPointRepository challengeCheckPointRepository;
    ChallengeCheckpointMapper challengeCheckpointMapper;

    @Override
    @Transactional
    public List<ChallengeCheckpointResponse> createChallengeCheckpoint(
            Long challengeId,
            List<AdminCreateChallengeCheckpointRequest> request
    ) {
        ChallengeRoute route = challengeRouteRepository
                .findByChallenge_Id(challengeId)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_ROUTE_NOT_FOUND));

        List<Integer> orders = request.stream()
                .map(AdminCreateChallengeCheckpointRequest::getOrderIndex)
                .sorted()
                .toList();

        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i) != i + 1) {
                throw new AppException(ErrorEnum.INVALID_CHECKPOINT_ORDER);
            }
        }

        List<ChallengeCheckpoint> checkpoints = request.stream()
                .map(req -> {
                    ChallengeCheckpoint checkpoint = challengeCheckpointMapper.toEntity(req);
                    checkpoint.setRoute(route);
                    return checkpoint;
                })
                .toList();

        List<ChallengeCheckpoint> saved = challengeCheckPointRepository.saveAll(checkpoints);

        return saved.stream()
                .map(challengeCheckpointMapper::EntityToRespond)
                .toList();
    }
}
