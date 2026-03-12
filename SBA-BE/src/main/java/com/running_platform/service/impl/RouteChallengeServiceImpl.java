package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.AdminCreateRouteChallengeRequest;
import com.running_platform.dto.request.AdminUpdateRouteChallengeRequest;
import com.running_platform.dto.response.AdminRouteChallengeResponse;
import com.running_platform.dto.response.PageResponse;
import com.running_platform.entity.RouteChallege.RouteChallenge;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.RouteChallengeMapper;
import com.running_platform.repository.RouteChallengeRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.RouteChallengeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteChallengeServiceImpl implements RouteChallengeService {

    RouteChallengeRepository routeChallengeRepository;
    RouteChallengeMapper routeChallengeMapper;
    UserRepository userRepository;

    @Override
    public AdminRouteChallengeResponse createRouteChallenge(
            AdminCreateRouteChallengeRequest request
    ) {

        RouteChallenge challenge = routeChallengeMapper.toEntity(request);

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        challenge.setCreator(user);

        challenge.setStatus(ChallengeStatus.ACTIVE);

        routeChallengeRepository.save(challenge);

        return routeChallengeMapper.toResponse(challenge);
    }

    @Override
    public PageResponse<AdminRouteChallengeResponse> getRouteChallenges(
            int page,
            int size
    ) {

        Page<RouteChallenge> challengePage =
                routeChallengeRepository.findAll(PageRequest.of(page, size));

        Page<AdminRouteChallengeResponse> dtoPage =
                challengePage.map(routeChallengeMapper::toResponse);

        return PageResponse.from(dtoPage);
    }

    @Override
    public AdminRouteChallengeResponse getRouteChallengeById(Long id) {

        RouteChallenge challenge = routeChallengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        return routeChallengeMapper.toResponse(challenge);
    }

    @Override
    public AdminRouteChallengeResponse updateRouteChallenge(
            Long id,
            AdminUpdateRouteChallengeRequest request
    ) {

        RouteChallenge challenge = routeChallengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        routeChallengeMapper.updateEntity(request, challenge);

        routeChallengeRepository.save(challenge);

        return routeChallengeMapper.toResponse(challenge);
    }

    @Override
    public void deleteRouteChallenge(Long id) {

        RouteChallenge challenge = routeChallengeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        challenge.setIsDeleted(true);

        routeChallengeRepository.save(challenge);
    }
}
