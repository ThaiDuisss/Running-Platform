package com.running_platform.dto.response.challenge;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeRouteResponse {

    String polyline;

    Double totalDistance;

    Integer requiredLoops;

    ChallengeCheckpointResponse checkpoint;

}
