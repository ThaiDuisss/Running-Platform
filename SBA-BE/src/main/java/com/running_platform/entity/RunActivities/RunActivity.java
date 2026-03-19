package com.running_platform.entity.RunActivities;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Where(clause = "is_deleted=false")
@NoArgsConstructor
public class RunActivity extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = true)
    UserPlans plan;

    BigDecimal distance;

    Integer duration;

    BigDecimal avgPace;

    @Column(columnDefinition = "TEXT")
    String polyline;

    BigDecimal startLat;

    BigDecimal startLng;

    @Column(name = "startDatetime")
    public Date startDatetime;

    /**
     * The end datetime of the activity.
     */
    @Column(name = "endDatetime")
    public Date endDatetime;

    BigDecimal AvgPace;

    String pace;
//
//    public String getPace(){
//
//        if(distance == null || duration == null) return null;
//
//        double pace = duration / distance.doubleValue();
//
//        int minutes = (int) pace;
//        int seconds = (int) ((pace - minutes) * 60);
//
//        return String.format("%d:%02d", minutes, seconds);
//    }
}
