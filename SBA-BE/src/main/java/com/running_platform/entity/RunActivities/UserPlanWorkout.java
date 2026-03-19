package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_plan_workout",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"created_by", "scheduled_date"}
        ))
public class UserPlanWorkout extends AbstractEntity<Long> {

    @Column(nullable = false)
    LocalDate scheduledDate;

    BigDecimal targetDistance;

    @Column(length = 255)
    String title;

    LocalTime startTime;

    LocalTime endTime;

    boolean isSetTime;

    Double duration;

    boolean isCompleted;
}