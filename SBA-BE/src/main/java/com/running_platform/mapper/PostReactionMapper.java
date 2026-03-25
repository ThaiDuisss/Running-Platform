package com.running_platform.mapper;

import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.Post.PostReactions;
import com.running_platform.entity.Post.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostReactionMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "post.id", target = "postId")
    PostReactionResponse toDto(PostReactions postReaction);
}
