package com.running_platform.service;

import com.running_platform.dto.response.CommentReactionResponse;
import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.enums.ReactionType;
import org.springframework.data.domain.Page;

public interface CommentReactionsService {
    CommentReactionResponse reactToComment(Long commentId, Long userId, ReactionType type);
    long countReactFromComment(Long postId);
    Page<CommentReactionResponse> getReactionsByCommentId(Long postId, int page, int size);

}
