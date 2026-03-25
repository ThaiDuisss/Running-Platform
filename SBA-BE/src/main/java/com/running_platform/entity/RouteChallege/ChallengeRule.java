package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.ChallengeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

// luật chơi của tùng thử thách, có thể là: chạy 100km trong 30 ngày, chạy 5km mỗi ngày.
@Entity
@Table(name = "challenge_rule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChallengeRule extends AbstractEntity<Long> {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false, unique = true)
    Challenge challenge;

    @Enumerated(EnumType.STRING)
    ChallengeType type;

    Double targetValue;      // 100km, 7 runs

    Integer durationDays;

    Double dailyTarget;
    Double minSpeed;

}
