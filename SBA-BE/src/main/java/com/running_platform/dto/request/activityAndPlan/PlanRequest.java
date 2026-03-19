package com.running_platform.dto.request.activityAndPlan;

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

public class PlanRequest {

    LocalDate scheduledDate;

    BigDecimal targetDistance;

    String title;

    LocalTime startTime;

    LocalTime endTime;

    boolean isSetTime;

    double duration;

    boolean isCompleted;
}
