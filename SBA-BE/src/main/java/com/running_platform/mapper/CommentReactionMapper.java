package com.running_platform.mapper;

import com.running_platform.dto.response.CommentReactionResponse;
import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.Post.CommentReactions;
import com.running_platform.entity.Post.PostReactions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentReactionMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "comment.id", target = "commentId")
    CommentReactionResponse toDto(CommentReactions commentReactions);
}