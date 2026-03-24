package com.running_platform.service.impl.Challenge;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.challenge.AdminCreateChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.entity.RouteChallege.Challenge;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.challenge.ChallengeMapper;
import com.running_platform.repository.Challenge.ChallengeRepository;
import com.running_platform.repository.Challenge.ChallengeRuleRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.Challenge.ChallengeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChallengeServiceImpl implements ChallengeService {

    ChallengeRepository challengeRepository;
    ChallengeRuleRepository challengeRuleRepository;
    ChallengeMapper challengeMapper;
    UserRepository userRepository;

    @Transactional
    @Override
    public ChallengeResponse createChallenge(
            AdminCreateChallengeRequest request
    ) {

        Long userId = AppSecurityUtils.getCurrentUserId()
                .orElseThrow(() -> new AppException(ErrorEnum.UNAUTHORIZED));

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        // business validation
        if (challengeRepository.existsByTitleAndCreatorId(request.getTitle(), userId)) {
            throw new AppException(ErrorEnum.CHALLENGE_ALREADY_EXISTS);
        }

        // mapping
        Challenge challenge = challengeMapper.toEntity(request);

        // set fields
        challenge.setCreator(user);
        challenge.setStatus(ChallengeStatus.DRAFT);

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

//    @Override
//    public PageResponse<AdminRouteChallengeResponse> getRouteChallenges(
//            int page,
//            int size
//    ) {
//
//        Page<Challenge> challengePage =
//                routeChallengeRepository.findAll(PageRequest.of(page, size));
//
//        Page<AdminRouteChallengeResponse> dtoPage =
//                challengePage.map(routeChallengeMapper::toResponse);
//
//        return PageResponse.from(dtoPage);
//    }
//
//    @Override
//    public AdminRouteChallengeResponse getRouteChallengeById(Long id) {
//
//        Challenge challenge = routeChallengeRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
//
//        return routeChallengeMapper.toResponse(challenge);
//    }
//
//    @Override
//    public AdminRouteChallengeResponse updateRouteChallenge(
//            Long id,
//            AdminUpdateRouteChallengeRequest request
//    ) {
//
//        Challenge challenge = routeChallengeRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
//
//        routeChallengeMapper.updateEntity(request, challenge);
//
//        routeChallengeRepository.save(challenge);
//
//        return routeChallengeMapper.toResponse(challenge);
//    }
//
//    @Override
//    public void deleteRouteChallenge(Long id) {
//
//        Challenge challenge = routeChallengeRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
//
//        challenge.setIsDeleted(true);
//
//        routeChallengeRepository.save(challenge);
//    }
}
