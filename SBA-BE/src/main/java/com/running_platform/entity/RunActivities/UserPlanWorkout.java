package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_plan_id", "scheduledDate"}
))
public class UserPlanWorkout extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id")
    @Nullable
    RunActivity runActivity;

    LocalDate scheduledDate;

    BigDecimal targetDistance;
    @Column(nullable = true)

    LocalTime startTime;
    @Column(nullable = true)

    LocalTime endTime;

    boolean isSetTime;

    Double duration;

    @Column(nullable = true)
    boolean isCompleted;
}
