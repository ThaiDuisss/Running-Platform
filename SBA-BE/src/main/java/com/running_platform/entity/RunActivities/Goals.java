package com.running_platform.entity.RunActivities;


import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.GoalType;
import com.running_platform.enums.IconEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "goals")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "is_deleted=false")
public class Goals extends AbstractEntity<Long> {

    @Column(unique = true, nullable = false)
    String key;

    @Column(nullable = false)
    String title;

    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type")
    GoalType goalType;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    Map<String, Object> params;

    @Enumerated(EnumType.STRING)
    IconEnum icon;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<PlanTemplates> planTemplates;
}