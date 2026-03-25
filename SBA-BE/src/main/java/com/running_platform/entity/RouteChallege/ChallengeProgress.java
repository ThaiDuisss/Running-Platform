package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
@Table(
        name = "challenge_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "challenge_id"})
        }
)
public class ChallengeProgress extends AbstractEntity<Long> {

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "challenge_id", nullable = false)
    Long challengeId;

    Integer currentCheckpointIndex;

    Integer completedLoops;

    Boolean completed;
}