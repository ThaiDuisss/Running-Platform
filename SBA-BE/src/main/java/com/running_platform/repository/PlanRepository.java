package com.running_platform.repository;

import com.running_platform.entity.RunActivities.UserPlanWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<UserPlanWorkout, Long> {
    List<UserPlanWorkout> findByScheduledDateBetween(LocalDate start, LocalDate end);
}
