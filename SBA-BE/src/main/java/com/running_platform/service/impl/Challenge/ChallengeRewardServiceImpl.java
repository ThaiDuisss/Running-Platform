package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRewardRequest;
import com.running_platform.dto.response.challenge.ChallengeRewardResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.RouteChallege.ChallengeReward;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeRewardMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.Challenge.ChallengeRewardRepository;
import com.running_platform.service.Challenge.ChallengeRewardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeRewardServiceImpl implements ChallengeRewardService {

    ChallengeRewardRepository challengeRewardRepository;
    ChallengeRepository challengeRepository;
    ChallengeRewardMapper challengeRewardMapper;

    @Override
    @Transactional
    public ChallengeRewardResponse createChallengeReward(
            Long challengeId,
            AdminCreateChallengeRewardRequest request
    ) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challengeRewardRepository.existsByChallenge_Id(challengeId)) {
            throw new AppException(ErrorEnum.REWARD_ALREADY_EXISTS);
        }

        ChallengeReward challengeReward = challengeRewardMapper.toEntity(request);

        challengeReward.setChallenge(challenge);

        ChallengeReward saved = challengeRewardRepository.save(challengeReward);

        return challengeRewardMapper.EntityToRespond(saved);
    }
}
