package com.laundry.entity.RunActivities;

import com.laundry.entity.AbstractEntity;
import com.laundry.enums.DayOfWeekEnum;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_template_id", nullable = false)
    PlanTemplates planTemplate;

    Integer weekNumber;

    @Enumerated(EnumType.STRING)
    DayOfWeekEnum dayOfWeek;

    BigDecimal targetDistance;
}
