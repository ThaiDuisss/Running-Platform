package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRuleRequest;
import com.running_platform.dto.response.challenge.ChallengeRuleResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.RouteChallege.ChallengeRule;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeRuleMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.Challenge.ChallengeRuleRepository;
import com.running_platform.service.Challenge.ChallengeRuleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeRuleServiceImpl implements ChallengeRuleService {

    ChallengeRuleRepository challengeRuleRepository;
    ChallengeRepository challengeRepository;
    ChallengeRuleMapper challengeRuleMapper;

    @Override
    @Transactional
    public ChallengeRuleResponse createChallengeRule(
            Long challengeId,
            AdminCreateChallengeRuleRequest request
    ) {

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() != ChallengeStatus.DRAFT) {
            throw new AppException(ErrorEnum.INVALID_REQUEST);
        }

        if (challengeRuleRepository.existsByChallenge_Id(challengeId)) {
            throw new AppException(ErrorEnum.RULE_ALREADY_EXISTS);
        }

        validateRule(request);

        ChallengeRule challengeRule = challengeRuleMapper.toEntity(request);
        challengeRule.setChallenge(challenge);

        ChallengeRule saved = challengeRuleRepository.save(challengeRule);

        return challengeRuleMapper.EntityToRespond(saved);
    }

    private void validateRule(AdminCreateChallengeRuleRequest request) {
        if (request.getType() == null) {
            throw new AppException(ErrorEnum.INVALID_REQUEST);
        }

        switch (request.getType()) {
            case TOTAL_DISTANCE -> {
                if (request.getTargetValue() == null) {
                    throw new AppException(ErrorEnum.INVALID_REQUEST);
                }
            }
            case DAILY_DISTANCE -> {
                if (request.getDailyTarget() == null) {
                    throw new AppException(ErrorEnum.INVALID_REQUEST);
                }
            }
            case SPEED_CHALLENGE -> {
                if (request.getMinSpeed() == null) {
                    throw new AppException(ErrorEnum.INVALID_REQUEST);
                }
            }
        }
    }
}
