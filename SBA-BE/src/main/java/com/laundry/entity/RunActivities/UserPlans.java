package com.laundry.entity.RunActivities;

import com.laundry.entity.AbstractEntity;
import com.laundry.entity.UserAuth.Users;
import com.laundry.enums.VisibilityEnum;
import jakarta.persistence.*;
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
public class UserPlans extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_template_id", nullable = true)
    PlanTemplates planTemplate;

    @Column(length = 500)
    String title;

    @Lob
    String description;

    Instant startDate;

    Instant endDate;

    @Enumerated(EnumType.STRING)
    VisibilityEnum visibility;
}
