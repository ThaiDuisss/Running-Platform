package com.running_platform.dto.request.activityAndPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.running_platform.enums.LevelEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanCustomRequest {
    String title;
    Long goal;
    int durationWeeks;
    int daysPerWeek;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    LocalDateTime dayStart;
    LevelEnum selectedLevel;
    Map<String, Object> params;
    List<Days> schedule;
//@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
//
//    public static class Week {
//        List<Days> days;
//        private int week;
//
//    }
    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)

    public static class Days {
        LocalDate date;
        String dayName;
        String type;
        int targetDistance;
        int duration;
    }
}


