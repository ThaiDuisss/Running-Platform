package com.running_platform.dto.request.challenge;

import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.ChallengeType;
import com.running_platform.enums.VisibilityEnum;
import com.running_platform.validation.annotation.ValidTimeRange;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidTimeRange
public class FilterChallengeRequest {
    String title;

    String description;

    Instant startTime;

    Instant endTime;

    VisibilityEnum visibility;

    ChallengeStatus status;

    //of ChallengeRule
    ChallengeType type;
}
