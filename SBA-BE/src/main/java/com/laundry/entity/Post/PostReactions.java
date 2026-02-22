package com.laundry.entity.Post;

import com.laundry.entity.AbstractEntity;
import com.laundry.entity.UserAuth.Users;
import com.laundry.enums.ReactionType;
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
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"post_id", "user_id"}
        )
)
public class PostReactions extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false )
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id", nullable = false )
    Posts post;

    @Enumerated(EnumType.STRING)
    private ReactionType reaction;
}
