package com.running_platform.dto.response.activityAndPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PlanResponse {

    Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate scheduledDate;

    int targetDistance;

    String title;

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    LocalTime endTime;

    @JsonProperty("isSetTime")
    boolean isSetTime;

    int duration;

    boolean isCompleted;


}