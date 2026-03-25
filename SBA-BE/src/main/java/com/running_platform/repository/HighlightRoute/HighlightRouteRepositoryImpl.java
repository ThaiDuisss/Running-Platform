package com.running_platform.repository.HighlightRoute;

import com.querydsl.core.BooleanBuilder;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.running_platform.dto.request.highlightRoute.HighlightRouteRequest;
import com.running_platform.entity.RunActivities.HighlightRoute;
import com.running_platform.entity.RunActivities.QHighlightRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HighlightRouteRepositoryImpl implements HighlightRouteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HighlightRoute> search(HighlightRouteRequest request, Pageable pageable) {

        QHighlightRoute route = QHighlightRoute.highlightRoute;

        BooleanBuilder builder = new BooleanBuilder();

        if (request.getTitle() != null) {
            builder.and(route.title.containsIgnoreCase(request.getTitle()));
        }

        if (request.getLocation() != null) {
            builder.and(route.location.containsIgnoreCase(request.getLocation()));
        }

        if (request.getIsActive() != null) {
            builder.and(route.isActive.eq(request.getIsActive()));
        }

        if (request.getPriority() != null) {
            builder.and(route.priority.goe(request.getPriority()));
        }

        List<HighlightRoute> content = queryFactory
                .selectFrom(route)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(route.priority.asc())
                .fetch();

        Long total = queryFactory
                .select(route.count())
                .from(route)
                .where(builder)
                .fetchOne();

        long totalElements = total != null ? total : 0L;

        return new PageImpl<>(content, pageable, totalElements);
    }
}
