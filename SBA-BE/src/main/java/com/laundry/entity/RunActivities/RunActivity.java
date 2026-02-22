package com.laundry.entity.RunActivities;

import com.laundry.entity.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Where(clause = "is_deleted=false")
public class RunActivity extends AbstractEntity<Long> {
}
