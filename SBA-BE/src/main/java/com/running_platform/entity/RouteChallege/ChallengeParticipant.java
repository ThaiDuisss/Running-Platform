package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.time.Instant;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class ChallengeParticipant extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    Users participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RouteChallenge_id", nullable = false)
    RouteChallenge RouteChallenge;

    @Enumerated(EnumType.STRING)
    ParticipantStatus status;

    Instant completeAt;
}
