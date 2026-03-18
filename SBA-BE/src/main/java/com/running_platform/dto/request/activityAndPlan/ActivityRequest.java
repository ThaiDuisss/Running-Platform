package com.running_platform.dto.request.activityAndPlan;

import canape.benjamin.runflutterrun.model.enums.ActivityType;
import com.running_platform.entity.RunActivities.UserPlans;
import com.running_platform.entity.UserAuth.Users;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Data Transfer Object (DTO) for representing an activity.
 */
@Data
public class ActivityResponse {

    Long planId;

    BigDecimal distance;

    Integer duration;

    BigDecimal avgPace;

    String polyline;

    BigDecimal startLat;

    BigDecimal startLng;

    
}
