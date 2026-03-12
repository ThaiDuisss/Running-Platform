package com.running_platform.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {

    Long id;

    String username;

    String content;

    List<String> mediaUrls;

    Long runId;

    Long challengeId;

    LocalDateTime createdAt;

}