package com.running_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.running_platform.enums.ChallengeStatus;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminRouteChallengeResponse {
    private Long id;

    private String title;

    private String description;

    private ChallengeType type;

    private BigDecimal targetValue;

    private Instant startTime;

    private Instant endTime;

    private VisibilityEnum visibility;

    private ChallengeStatus status;

    private Instant createdAt;
}
