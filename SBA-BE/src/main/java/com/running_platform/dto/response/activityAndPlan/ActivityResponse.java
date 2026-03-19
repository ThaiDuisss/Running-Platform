package com.running_platform.dto.response.activityAndPlan;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityResponse {

    Long id;

    Long planId;

    Double distance;

    Integer duration;

    Double avgPace;

    Double avgSpeed;

    Double startLat;

    Double startLng;

    String polyline;

     Date startDatetime;

     Date endDatetime;

//    Integer calories;

}