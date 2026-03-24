package com.running_platform.dto.response.challenge;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeResponse {

    Long id;

    String title;

    String description;

    Instant startTime;

    Instant endTime;

    VisibilityEnum visibility;

    ChallengeStatus status;

}
