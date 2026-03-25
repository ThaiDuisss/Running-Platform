package com.running_platform.mapper;

import com.running_platform.dto.response.PostCommentsResponse;
import com.running_platform.entity.Post.PostComments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostCommentsMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "replies", ignore = true)
    PostCommentsResponse toDto(PostComments postComments);

}
