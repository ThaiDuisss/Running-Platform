package com.running_platform.dto.response;

import com.running_platform.enums.PostStatus;
import com.running_platform.enums.VisibilityEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {

    Long id;

    String username;

    String content;

    PostStatus status;

    List<String> images;
}