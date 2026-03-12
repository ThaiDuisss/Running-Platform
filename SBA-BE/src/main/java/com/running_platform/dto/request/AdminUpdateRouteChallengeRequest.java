package com.running_platform.dto.request;

import com.running_platform.enums.ChallengeType;
import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateRouteChallengeRequest {

    String title;

    String description;

    ChallengeType challengeType;

    BigDecimal targetValue;

    Instant startTime;

    Instant endTime;

    VisibilityEnum visibility;

}
