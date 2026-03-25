package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.DayOfWeekEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"plan_template_id", "week_number","day_of_week"}
))
public class TemplateWorkouts extends AbstractEntity<Long> {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "template_workout_plan",
            joinColumns = @JoinColumn(name = "template_workout_id"),
            inverseJoinColumns = @JoinColumn(name = "plan_template_id")
    )
    List<PlanTemplates> planTemplate;

//    @Enumerated(EnumType.STRING)
//    DayOfWeekEnum dayOfWeek;


    BigDecimal targetDistance;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plan_workout_id", nullable = true)
    List<UserPlanWorkout> userPlanWorkout;
}
