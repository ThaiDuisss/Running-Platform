package com.running_platform.dto.response;

import com.running_platform.enums.CategoryEnum;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ArticleResponse {
    private Long id;

    private String title;

    private String summary;

    private String content;

    private String thumbnailUrl;

    private LocalDateTime publishedAt;

    private CategoryEnum category;
}
