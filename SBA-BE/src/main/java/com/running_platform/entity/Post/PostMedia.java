package com.running_platform.entity.Post;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.entity.Files;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class PostMedia extends AbstractEntity<Long> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    Posts posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    Files file;
}
