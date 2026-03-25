package com.running_platform.dto.response;

import com.running_platform.entity.Post.PostComments;
import com.running_platform.repository.PostCommentsRepository;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class PostCommentsResponse {
    Long id;
    String username;
    String content;
    Instant createdAt;

    List<PostCommentsResponse> replies;
}
