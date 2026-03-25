package com.running_platform.repository;

import com.running_platform.entity.RunActivities.Goals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goals, Long> {

    List<Goals> findByIsActiveTrueAndIsDeletedFalse();
}