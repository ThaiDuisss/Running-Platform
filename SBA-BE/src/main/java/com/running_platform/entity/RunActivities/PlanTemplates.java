package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.GoalType;
import com.running_platform.enums.LevelEnum;
import com.running_platform.enums.VisibilityEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

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

    @Lob
    private String description;
}
