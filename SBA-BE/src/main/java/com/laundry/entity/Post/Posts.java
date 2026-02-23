package com.laundry.entity.Post;

import com.laundry.entity.AbstractEntity;
import com.laundry.entity.RouteChallege.RouteChallenge;
import com.laundry.entity.RunActivities.RunActivity;
import com.laundry.entity.UserAuth.Users;
import com.laundry.enums.PostStatus;
import com.laundry.enums.VisibilityEnum;
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
public class Posts extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @Lob
    String content;

    @Enumerated(EnumType.STRING)
    VisibilityEnum visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    RunActivity run;

    @ManyToOne(fetch = FetchType.LAZY)
    RouteChallenge challenge;

    @Enumerated(EnumType.STRING)
    PostStatus status;
}
