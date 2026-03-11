package com.running_platform.dto.response;

import com.running_platform.enums.PostStatus;
import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminPostResponse {
    Long id;

    String username;

    String content;

    VisibilityEnum visibility;

    PostStatus status;

    Long runId;

    Long challengeId;

    Instant createdAt;

}