package com.running_platform.dto.request.challenge;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.NonNull;

import java.util.List;

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

    List<AdminCreateChallengeCheckpointRequest> checkpoints;
}