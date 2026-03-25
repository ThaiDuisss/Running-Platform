package com.running_platform.dto.response;

import com.running_platform.enums.PostStatus;
import com.running_platform.enums.ReactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {

    Long id;
    String username;
    String content;
    PostStatus status;
    List<String> images;
    Instant createDate;
    Long totalReactions;
    ReactionType currentUserReact;

    public PostResponse(Object id, String username, String content, PostStatus status, Instant createDate, Long totalReactions, ReactionType currentUserReact) {
        if (id instanceof Number) {
            this.id = ((Number) id).longValue();
        } else if (id != null) {
            this.id = Long.parseLong(id.toString());
        }
        this.username = username;
        this.content = content;
        this.status = status;
        this.createDate = createDate;
        this.totalReactions = totalReactions;
        this.currentUserReact = currentUserReact;
        this.images = new ArrayList<>();
    }
}