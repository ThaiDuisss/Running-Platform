package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.LevelEnum;
import com.running_platform.enums.VisibilityEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

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

    @Enumerated(EnumType.STRING)
    @Nullable
    LevelEnum level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = true)
    Goals goal;

    int durationWeeks;

    @Enumerated(EnumType.STRING)
    VisibilityEnum visibilityEnum;

    @Lob
    private String description;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> params;

    @ManyToMany(mappedBy = "planTemplate")
    List<TemplateWorkouts> templateWorkouts;

}
