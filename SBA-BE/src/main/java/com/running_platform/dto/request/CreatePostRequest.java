package com.running_platform.dto.request;

import com.running_platform.enums.PostStatus;
import com.running_platform.enums.VisibilityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {

    private String content;

    private PostStatus status;

    private List<String> images;}