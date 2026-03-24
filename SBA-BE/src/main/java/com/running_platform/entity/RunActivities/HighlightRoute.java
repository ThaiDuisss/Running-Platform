package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "highlight_route")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Where(clause = "is_deleted=false")
public class HighlightRoute extends AbstractEntity<Long> {

    String title;

    String thumbnail;

    String location;

    String distanceLabel;

    Integer priority;

    Boolean isActive;

}
