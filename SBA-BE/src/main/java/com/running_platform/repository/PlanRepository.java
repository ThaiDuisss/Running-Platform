package com.running_platform.repository;

import com.running_platform.entity.RunActivities.UserPlanWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<UserPlanWorkout, Long> {

    Optional<UserPlanWorkout> findByIdAndCreatedBy_Id(Long id, Long userId);

    List<UserPlanWorkout> findByScheduledDateBetweenAndCreatedBy_Id(
            LocalDate start, LocalDate end, Long userId
    );
}