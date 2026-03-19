package com.running_platform.service.impl.activityAndPlan;
import com.running_platform.constant.ErrorEnum;
import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.exception.AppException;
import com.running_platform.repository.ActivityRepository;
import com.running_platform.security.AppSecurityUtils;
import com.running_platform.service.IActivityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ActivityServiceImpl implements IActivityService {

    private final com.running_platform.repository.UserRepository userRepository;
    private final ActivityRepository activityRepository;

    @Override
    public Iterable<RunActivity> getAll() {
        return null;
    }

    @Override
    public RunActivity create(RunActivity activity) {
        RunActivity activityWithMetrics = calculateMetrics(activity);
        return activityRepository.save(activityWithMetrics);
    }

    @Override
    public Page<RunActivity> getAll(Pageable pageable) {
        Optional<Long> id = AppSecurityUtils.getCurrentUserId();
        return activityRepository.findByUser_Id(id.get(), pageable);
    }

    @Override
    public Page<RunActivity> getByUser(Long userId, Pageable pageable) {
        return activityRepository.findByUser_Id(userId, pageable);
    }


    @Override
    public RunActivity getById(long id) {
        RunActivity activity = activityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Activity with id: " + id + " is not available."));
        Long userId = AppSecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));
        if (activity.getUser().getId().equals(id)){
            return activity;
        }

        throw new SecurityException("You don't have the right to retrieve this activity");
    }


    @Override
    public RunActivity update( RunActivity activity) {

        RunActivity existingActivity = activityRepository.findById(activity.getId()).orElseThrow(
                () -> new EntityNotFoundException("Activity with id: " + activity.getId() + " is not available."));
        Long userId = AppSecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        if(existingActivity.getUser().getId().equals(userId)) {
            RunActivity updatedActivity = calculateMetrics(activity);
            existingActivity.setDistance(updatedActivity.getDistance());
            existingActivity.setStartDatetime(updatedActivity.getStartDatetime());
            existingActivity.setEndDatetime(updatedActivity.getEndDatetime());
            existingActivity.setAvgPace(updatedActivity.getAvgPace());

            return activityRepository.save(existingActivity);
        }

        throw new SecurityException("You don't have the right to update this activity");
    }


    @Override
    public void delete(long id) {
        Long userId = AppSecurityUtils.getCurrentUserId().orElseThrow(() -> new AppException(ErrorEnum.UNKNOWN_ERROR));

        RunActivity activity = getById(id);
        if(activity.getUser().getId().equals(userId)) {
            activityRepository.deleteById(id);
            return;
        }

        throw new SecurityException("You don't have the right to delete this activity");
    }




    private RunActivity calculateMetrics(RunActivity activity) {
        Date start = activity.getStartDatetime();
        Date end = activity.getEndDatetime();

        long time = end.getTime() - start.getTime(); // milliseconds

        if (time > 0 && activity.getDistance().doubleValue() > 0) {

            double durationSeconds = time / 1000.0;

            double avgPace = durationSeconds / activity.getDistance().doubleValue();
            // seconds per km

            activity.setAvgPace(BigDecimal.valueOf(avgPace));
        }

        return activity;
    }
}
