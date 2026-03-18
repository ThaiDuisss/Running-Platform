package com.running_platform.dto.response;

import com.running_platform.enums.PostStatus;
import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Setter
public class AdminPostResponse {
    Long id;

    String username;

    String content;

    PostStatus status;

    List<String> images;

}