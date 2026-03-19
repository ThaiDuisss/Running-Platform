package com.running_platform.dto.response.activityAndPlan;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    PlanResponse {
    Long id;

    LocalDate scheduledDate;

    BigDecimal targetDistance;

    LocalTime title;

    LocalTime startTime;

    LocalTime endTime;

    boolean isSetTime;

    double duration;

    boolean isCompleted;
}
