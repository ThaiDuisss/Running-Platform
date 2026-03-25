package com.running_platform.service;

import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.entity.RunActivities.UserPlanWorkout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IActivityService extends ICrudService<RunActivity> {
    Page<RunActivity> getAll(Pageable pageable);
    Page<RunActivity> getByUser( Long userId, Pageable pageable);
}