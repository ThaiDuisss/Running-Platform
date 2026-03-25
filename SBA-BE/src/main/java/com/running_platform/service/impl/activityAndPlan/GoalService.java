package com.running_platform.service.impl.activityAndPlan;

import com.running_platform.dto.response.ApiResponse;
import com.running_platform.dto.response.GoalResponse;
import com.running_platform.entity.RunActivities.Goals;
import com.running_platform.repository.GoalRepository;
import com.running_platform.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalsRepository;
    private final RedisService redisService;
    @Cacheable(value = "goals", key = "'all'")
    public ApiResponse<List<GoalResponse>> getAllActiveGoals() {
        System.out.println("🔥 Query DB");

        List<GoalResponse> list = goalsRepository.findByIsActiveTrueAndIsDeletedFalse()
                .stream()
                .map(g -> GoalResponse.builder()
                        .id(g.getId())
                        .key(g.getKey())
                        .title(g.getTitle())
                        .description(g.getDescription())
                        .goalType(g.getGoalType().name())
                        .icon(g.getIcon().name())
                        .params(g.getParams())
                        .build())
                .toList();

        return ApiResponse.<List<GoalResponse>>builder()
                .data(list)
                .build();
    }
}