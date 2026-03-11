package com.running_platform.dto.request;

import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {

    String content;

    VisibilityEnum visibility;

    Long runId;

    Long challengeId;

    List<String> mediaUrls;

}