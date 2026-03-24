package com.running_platform.dto.request.challenge;

import com.running_platform.enums.ChallengeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCreateChallengeRuleRequest {

    @NotNull
    ChallengeType type;

    @PositiveOrZero
    Double targetValue;

    @PositiveOrZero
    Integer durationDays;

    @PositiveOrZero
    Double dailyTarget;

    @PositiveOrZero
    Double minSpeed;
}
