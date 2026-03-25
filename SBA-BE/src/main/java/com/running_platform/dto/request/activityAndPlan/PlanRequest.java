package com.running_platform.dto.request.activityAndPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "Scheduled date is required")
    LocalDate scheduledDate;

    @Positive(message = "Target distance must be positive")
    int targetDistance;

    @NotBlank(message = "Title is required")
    String title;

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime endTime;

    @JsonProperty("isSetTime")
    boolean isSetTime;

    @Positive(message = "Duration must be positive")
    int duration;

//    boolean isCompleted;
}