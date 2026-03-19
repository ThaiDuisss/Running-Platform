package com.running_platform.dto.request.activityAndPlan;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Data Transfer Object (DTO) for representing an activity.
 */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityRequest {

    Long planId;

    Double distance;

    Integer duration;

    Double avgPace;

    Double startLat;

    Double startLng;

    String polyline;

    Date startDatetime;

    Date endDatetime;

//    Integer calories;
}
