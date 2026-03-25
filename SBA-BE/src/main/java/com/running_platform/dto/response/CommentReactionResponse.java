package com.running_platform.dto.response;

import com.running_platform.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionResponse {
    private String username;
    private Long commentId;
    private ReactionType reactionType;
    private Instant createdDate;
    private boolean isDeleted;

    public CommentReactionResponse(String username, Object commentId, ReactionType reaction, Instant createdDate, boolean isDeleted) {
        this.username = username;
        if (commentId instanceof Number) {
            this.commentId = ((Number) commentId).longValue();
        } else if (commentId != null) {
            this.commentId = Long.parseLong(commentId.toString());
        }
        this.reactionType = reaction;
        this.createdDate = createdDate;
        this.isDeleted = isDeleted;
    }
}