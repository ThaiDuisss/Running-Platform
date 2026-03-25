package com.running_platform.dto.request.challenge;

import com.running_platform.dto.response.challenge.ChallengeCheckpointResponse;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUpdateChallengeRouteRequest {

    @NonNull
    String polyline;

    @PositiveOrZero
    Double totalDistance;

    @NonNull
    Integer requiredLoops;

    @NonNull
    List<AdminCreateChallengeCheckpointRequest> checkpoints;
}
