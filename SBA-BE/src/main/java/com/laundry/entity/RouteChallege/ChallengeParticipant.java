package com.laundry.entity.RouteChallege;

import com.laundry.entity.AbstractEntity;
import com.laundry.entity.UserAuth.Users;
import com.laundry.enums.ParticipantStatus;
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
