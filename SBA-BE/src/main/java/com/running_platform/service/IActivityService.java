package com.running_platform.service;

import com.running_platform.entity.RunActivities.RunActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IActivityService extends ICrudService<RunActivity> {
    Page<RunActivity> getAll(Pageable pageable);
    Page<RunActivity> getByUser( Long userId, Pageable pageable);
}