package com.running_platform.entity.Post;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.RouteChallege.RouteChallenge;
import com.running_platform.entity.RunActivities.RunActivity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.PostStatus;
import com.running_platform.enums.VisibilityEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<PostImage> images = new ArrayList<>();


    public void addImage(PostImage image) {
        images.add(image);
        image.setPost(this);
    }
}
