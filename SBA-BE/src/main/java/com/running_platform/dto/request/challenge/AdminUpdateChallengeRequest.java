package com.running_platform.dto.request.challenge;

import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.VisibilityEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateChallengeRequest {

    String title;

    String description;

    Instant startTime;

    Instant endTime;

    VisibilityEnum visibility;

    ChallengeStatus status;

    @NotNull
    ChallengeRewardRequest reward;

    @NotNull
    ChallengeRuleRequest rule;
}
