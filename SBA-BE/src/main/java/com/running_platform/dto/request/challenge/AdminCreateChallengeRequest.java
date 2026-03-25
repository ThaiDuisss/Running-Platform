package com.running_platform.dto.request.challenge;

import com.running_platform.enums.VisibilityEnum;
import com.running_platform.validation.annotation.ValidTimeRange;
import jakarta.validation.constraints.NotBlank;
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
@ValidTimeRange // custom validation annotation to check if startTime is before endTime
public class AdminCreateChallengeRequest {
    @NotNull
    String title;

    @NotBlank
    String description;

    @NotNull
    Instant startTime;

    @NotNull
    Instant endTime;

    @NotNull
    VisibilityEnum visibility;

    @NotNull
    ChallengeRewardRequest reward;

    @NotNull
    ChallengeRuleRequest rule;

}
