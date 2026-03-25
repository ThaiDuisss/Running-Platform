package com.running_platform.repository;

import com.running_platform.entity.RunActivities.PlanTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanTemplateRepository extends JpaRepository<PlanTemplates, Long> {
}
