package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

// Các điểm checkpoint trên đường chạy.
@Entity
@Table(name = "challenge_checkpoint")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeCheckpoint extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_route_id", nullable = false)
    ChallengeRoute route;

    Double latitude;

    Double longitude;

    Double radius;

    Integer orderIndex;

    String name;

}
