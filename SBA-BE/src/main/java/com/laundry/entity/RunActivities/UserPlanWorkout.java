package com.laundry.entity.RunActivities;

import com.laundry.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    @JoinColumn(name = "user_plan_id",nullable = false)
    UserPlans plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id")
    RunActivity runActivity;

    LocalDate scheduledDate;

    BigDecimal targetDistance;

    boolean isCompleted;
}
