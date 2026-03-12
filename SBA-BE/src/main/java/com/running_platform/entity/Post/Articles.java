package com.running_platform.entity.Post;

import com.running_platform.entity.AbstractEntity;
import com.running_platform.enums.CategoryEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted=false")
@Table(name = "articles")
public class Articles extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String summary;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String thumbnailUrl;

    private LocalDateTime publishedAt;

    private CategoryEnum category;
}
