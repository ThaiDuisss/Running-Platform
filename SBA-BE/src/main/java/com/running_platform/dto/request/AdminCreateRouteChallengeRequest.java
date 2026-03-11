package com.running_platform.dto.request;

import com.running_platform.enums.ChallengeType;
import com.running_platform.enums.VisibilityEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class AdminCreateRouteChallengeRequest {
    @NotBlank
    String title;

    String description;

    @NotNull
    ChallengeType type;

    @NotNull
    BigDecimal targetValue;

    @NotNull
    Instant startTime;

    @NotNull
    Instant endTime;

    @NotNull
    VisibilityEnum visibility;

    @NotNull
    ChallengeType challengeType;
}
