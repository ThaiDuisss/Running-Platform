package com.running_platform.entity.RouteChallege;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ChallengeStatus;
import com.running_platform.enums.VisibilityEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.time.Instant;

// chứa thông tin cơ bản của thử thách
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class Challenge extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    Users creator;

    @Column(length = 200, nullable = false)
    String title;

    @Lob
    String description;

    Instant startTime;
    Instant endTime;

    @Enumerated(EnumType.STRING)
    VisibilityEnum visibility;

    @Enumerated(EnumType.STRING)
    ChallengeStatus status;

}
