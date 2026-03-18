package com.running_platform.dto.request.activityAndPlan;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.entity.RunActivities.UserPlans;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserPlanWorkoutResponse {

    LocalDate scheduledDate;

    BigDecimal targetDistance;

    LocalTime startTime;

    LocalTime endTime;

    LocalDateTime reminderTime;

    boolean isCompleted;
}
