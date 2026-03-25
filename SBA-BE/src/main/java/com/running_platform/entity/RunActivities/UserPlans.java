package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.DayOfWeekEnum;
import com.running_platform.enums.VisibilityEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class UserPlans extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_template_id", nullable = true)
    @Nullable
    PlanTemplates planTemplate;

    @Column(length = 500)
    String title;


//    @Enumerated(EnumType.STRING)
//    DayOfWeekEnum dayOfWeek;

    private LocalDate startDate;

    private LocalDate endDate;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_plan_workout_id", nullable = true)
    List<UserPlanWorkout> userPlanWorkout;
}
