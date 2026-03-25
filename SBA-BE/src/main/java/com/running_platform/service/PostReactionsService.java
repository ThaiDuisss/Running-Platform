package com.running_platform.service;

import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.enums.ReactionType;
import org.springframework.data.domain.Page;

public interface PostReactionsService {
    PostReactionResponse reactPost(Long userId, Long postId, ReactionType type);
    long countReactFromPost(Long postId);
    Page<PostReactionResponse> getReactionsByPostId(Long postId, int page, int size);
}
