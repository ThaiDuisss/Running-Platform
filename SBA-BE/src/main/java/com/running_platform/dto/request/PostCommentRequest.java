package com.running_platform.dto.request;

import lombok.Data;

@Data
public class PostCommentRequest {
    private Long postId;
    private Long parentCommentId;
    private String content;
}
