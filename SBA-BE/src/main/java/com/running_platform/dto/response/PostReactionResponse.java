package com.running_platform.dto.response;

import com.running_platform.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionResponse {
    private String username;
    private Long postId;
    private ReactionType reaction;
    private Instant createdDate;
    private boolean isDeleted;

    public PostReactionResponse(String username, Object postId, ReactionType reaction, Instant createdDate, boolean isDeleted) {
        this.username = username;
        if (postId instanceof Number) {
            this.postId = ((Number) postId).longValue();
        } else if (postId != null) {
            this.postId = Long.parseLong(postId.toString());
        }
        this.reaction = reaction;
        this.createdDate = createdDate;
        this.isDeleted = isDeleted;
    }
}
