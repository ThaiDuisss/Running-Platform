package com.running_platform.repository;

import com.running_platform.entity.RunActivities.UserPlans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlans, Long> {
}
