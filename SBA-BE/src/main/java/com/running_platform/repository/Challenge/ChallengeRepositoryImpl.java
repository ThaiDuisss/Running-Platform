package com.running_platform.repository.Challenge;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.running_platform.dto.request.challenge.FilterChallengeRequest;
import com.running_platform.dto.response.challenge.ChallengeResponse;
import com.running_platform.dto.response.challenge.ChallengeRewardResponse;
import com.running_platform.dto.response.challenge.ChallengeRuleResponse;
import com.running_platform.entity.RouteChallege.QChallenge;
import com.running_platform.entity.RouteChallege.QChallengeReward;
import com.running_platform.entity.RouteChallege.QChallengeRule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChallengeResponse> filter(FilterChallengeRequest request, int page, int size) {

        QChallenge challenge = QChallenge.challenge;
        QChallengeRule rule = QChallengeRule.challengeRule;
        QChallengeReward reward = QChallengeReward.challengeReward;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            builder.and(challenge.title.containsIgnoreCase(request.getTitle()));
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            builder.and(challenge.description.containsIgnoreCase(request.getDescription()));
        }

        if (request.getStartTime() != null) {
            builder.and(challenge.startTime.goe(request.getStartTime()));
        }

        if (request.getEndTime() != null) {
            builder.and(challenge.endTime.loe(request.getEndTime()));
        }

        if (request.getVisibility() != null) {
            builder.and(challenge.visibility.eq(request.getVisibility()));
        }

        if (request.getStatus() != null) {
            builder.and(challenge.status.eq(request.getStatus()));
        }

        if (request.getType() != null) {
            builder.and(rule.type.eq(request.getType()));
        }

        Pageable pageable = PageRequest.of(page, size);

        List<ChallengeResponse> content = queryFactory
                .select(Projections.fields(
                        ChallengeResponse.class,
                        challenge.id,
                        challenge.title,
                        challenge.description,
                        challenge.startTime,
                        challenge.endTime,
                        challenge.visibility,
                        challenge.status,

                        // rule
                        Projections.fields(
                                ChallengeRuleResponse.class,
                                rule.type.as("type"),
                                rule.targetValue.as("targetValue"),
                                rule.durationDays.as("durationDays"),
                                rule.dailyTarget.as("dailyTarget"),
                                rule.minSpeed.as("minSpeed")
                        ).as("rule"),

                        // reward
                        Projections.fields(
                                ChallengeRewardResponse.class,
                                reward.money.as("money"),
                                reward.badge.as("badge")
                        ).as("reward")

                        // route bỏ qua
                ))
                .from(challenge)
                .leftJoin(challenge.rule, rule)
                .leftJoin(challenge.reward, reward)
                .where(builder)
                .orderBy(challenge.creatDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(challenge.id.countDistinct())
                .from(challenge)
                .leftJoin(challenge.rule, rule)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}