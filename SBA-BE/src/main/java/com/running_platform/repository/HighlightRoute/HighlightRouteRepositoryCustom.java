package com.running_platform.repository.HighlightRoute;

import com.running_platform.dto.request.highlightRoute.HighlightRouteRequest;
import com.running_platform.entity.RunActivities.HighlightRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HighlightRouteRepositoryCustom {
    Page<HighlightRoute> search(HighlightRouteRequest request, Pageable pageable);
}
