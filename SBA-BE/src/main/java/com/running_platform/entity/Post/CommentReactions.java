package com.running_platform.entity.Post;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ReactionType;
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
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"user_id", "comment_id"}
))
public class CommentReactions extends AbstractEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    PostComments comment;

    @Enumerated(EnumType.STRING)
    ReactionType reactionType;
}
