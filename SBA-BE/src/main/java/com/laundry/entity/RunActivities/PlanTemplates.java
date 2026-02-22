package com.laundry.entity.RunActivities;

import com.laundry.entity.AbstractEntity;
import com.laundry.enums.GoalType;
import com.laundry.enums.LevelEnum;
import com.laundry.enums.VisibilityEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class PlanTemplates extends AbstractEntity<Long> {
    @Column(length = 500, nullable = false)
    String name;

    @Column(nullable = false)
    Integer duration_weeks;

    @Enumerated(EnumType.STRING)
    LevelEnum level;

    @Enumerated(EnumType.STRING)
    GoalType goal;

    @Enumerated(EnumType.STRING)
    VisibilityEnum visibilityEnum;
}
