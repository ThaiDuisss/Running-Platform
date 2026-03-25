package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminUpdateChallengeRequest;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.request.challenge.ChallengeRewardRequest;
import com.running_platform.dto.request.challenge.ChallengeRuleRequest;
import com.running_platform.dto.request.challenge.FilterChallengeRequest;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.RouteChallege.ChallengeReward;
import com.running_platform.entity.RouteChallege.ChallengeRule;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.ChallengeType;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeMapper;
import com.running_platform.mapper.challenge.ChallengeRewardMapper;
import com.running_platform.mapper.challenge.ChallengeRuleMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.ChallengeRuleRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.Challenge.ChallengeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeServiceImpl implements ChallengeService {

    ChallengeRepository challengeRepository;
    ChallengeRuleRepository challengeRuleRepository;
    ChallengeMapper challengeMapper;
    ChallengeRewardMapper challengeRewardMapper;
    ChallengeRuleMapper challengeRuleMapper;
    UserRepository userRepository;

    @Transactional
    @Override
    public ChallengeResponse createChallenge(AdminCreateChallengeRequest request) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        if (challengeRepository.existsByTitleAndCreatorId(request.getTitle(), userId)) {
            throw new AppException(ErrorEnum.CHALLENGE_ALREADY_EXISTS);
        }

        Challenge challenge = challengeMapper.toEntity(request);
        challenge.setCreator(user);
        challenge.setStatus(ChallengeStatus.DRAFT);

        ChallengeRule rule = buildRule(request, challenge);
        challenge.setRule(rule);

        ChallengeReward reward = buildReward(request, challenge);
        challenge.setReward(reward);

        Challenge saved = challengeRepository.save(challenge);

        return challengeMapper.EntityToRespond(saved);
    }

    @Override
    @Transactional
    public ChallengeResponse publish(Long id) {

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() != ChallengeStatus.DRAFT) {
            throw new AppException(ErrorEnum.INVALID_REQUEST);
        }

        challengeRuleRepository.findByChallenge_Id(id)
                .orElseThrow(() -> new AppException(ErrorEnum.RULE_REQUIRED));

        challenge.setStatus(ChallengeStatus.ACTIVE);

        Challenge saved = challengeRepository.save(challenge);

        return challengeMapper.EntityToRespond(saved);
    }

    @Override
    public PageResponse<ChallengeResponse> filter(
            FilterChallengeRequest request,
            int page,
            int size
    ) {

        Page<ChallengeResponse> responsePage = challengeRepository.filter(request, page, size);

        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() == ChallengeStatus.ACTIVE) {
            throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        challenge.setIsDeleted(true);

        challengeRepository.save(challenge);
    }

    @Override
    @Transactional
    public ChallengeResponse update(
            Long id,
            AdminUpdateChallengeRequest request) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        if (challenge.getStatus() == ChallengeStatus.ACTIVE
                || challenge.getStatus() == ChallengeStatus.COMPLETED) {
            throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        challengeMapper.update(challenge, request);

        challengeRepository.save(challenge);

        return challengeMapper.EntityToRespond(challenge);
    }

    @Override
    public ChallengeResponse getById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.CHALLENGE_NOT_FOUND));

        return challengeMapper.EntityToRespond(challenge);
    }




//================================== PRIVATE METHODS ==================================
    private ChallengeRule buildRule(AdminCreateChallengeRequest request, Challenge challenge) {

        if (request.getRule() == null) {
            throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        ChallengeRuleRequest ruleRequest = request.getRule();

        ChallengeRule ruleEntity = challengeRuleMapper.toEntity(ruleRequest);
        ruleEntity.setChallenge(challenge);

        ChallengeType type = request.getRule().getType();
        ruleEntity.setType(type);

        switch (type) {

            case TOTAL_DISTANCE:
                if (ruleRequest.getTargetValue() == null) {
                    throw new AppException(ErrorEnum.BAD_REQUEST);
                }
                ruleEntity.setTargetValue(ruleRequest.getTargetValue());
                break;

            case DAILY_DISTANCE:
                if (ruleRequest.getDailyTarget() == null) {
                    throw new AppException(ErrorEnum.BAD_REQUEST);
                }
                ruleEntity.setDailyTarget(ruleRequest.getDailyTarget());
                break;

            case RUN_STREAK:
                if (ruleRequest.getDurationDays() == null) {
                    throw new AppException(ErrorEnum.BAD_REQUEST);
                }
                ruleEntity.setDurationDays(ruleRequest.getDurationDays());
                break;

            default:
                throw new AppException(ErrorEnum.BAD_REQUEST);
        }

        return ruleEntity;
    }

    private ChallengeReward buildReward(AdminCreateChallengeRequest request, Challenge challenge) {

        if (request.getReward() == null) return null;

        ChallengeRewardRequest rewardRequest = request.getReward();

        ChallengeReward reward = challengeRewardMapper.toEntity(rewardRequest);

        reward.setChallenge(challenge);

        return reward;
    }
}
