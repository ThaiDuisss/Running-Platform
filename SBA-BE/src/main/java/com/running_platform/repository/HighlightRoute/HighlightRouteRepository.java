package com.running_platform.repository.HighlightRoute;

import com.running_platform.entity.RunActivities.HighlightRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighlightRouteRepository
        extends JpaRepository<HighlightRoute, Long>,
        HighlightRouteRepositoryCustom {
    Page<HighlightRoute> findByLocationAndIsActiveTrue(String location, Pageable pageable);
    Page<HighlightRoute> findAllByIsActiveTrue(Pageable pageable);
}