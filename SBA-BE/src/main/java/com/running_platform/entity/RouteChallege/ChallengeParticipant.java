package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ParticipantStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.time.Instant;

// bảng trung gian giữa User và Challenge, lưu thông tin về tiến độ của người tham gia trong thử thách
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
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    Challenge challenge;

    @Enumerated(EnumType.STRING)
    ParticipantStatus status;

    Double currentValue;     // km / count

    Double progress;         // %

    Integer checkpointIndex; // dùng cho route/checkpoint

    Instant joinedAt;

    Instant completedAt;
}
