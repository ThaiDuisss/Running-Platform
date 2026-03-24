package com.running_platform.dto.request.challenge;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCreateChallengeRouteRequest {

    String polyline;

    Double totalDistance;

    Integer requiredLoops;
}
