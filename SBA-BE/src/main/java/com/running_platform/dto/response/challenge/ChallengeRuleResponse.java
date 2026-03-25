package com.running_platform.dto.response.challenge;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.running_platform.enums.ChallengeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeRuleResponse {
    ChallengeType type;
    Double targetValue;
    Integer durationDays;
    Double dailyTarget;
    Double minSpeed;
}
