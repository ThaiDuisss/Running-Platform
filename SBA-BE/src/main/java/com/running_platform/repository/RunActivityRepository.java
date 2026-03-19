package com.running_platform.repository;

import com.running_platform.entity.RunActivities.RunActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunActivityRepository extends JpaRepository<RunActivity, Long> {
}
