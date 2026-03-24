package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

// đường chạy của thử thách, có thể là một polyline mã hóa hoặc một tập hợp các điểm GPS
@Entity
@Table(name = "challenge_route")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeRoute extends AbstractEntity<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    Challenge challenge;

    @Column(columnDefinition = "TEXT")
    String polyline;

    Double totalDistance;

    Integer requiredLoops;
}
