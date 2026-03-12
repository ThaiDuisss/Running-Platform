package com.running_platform.mapper;

import com.running_platform.dto.response.PostResponse;
import com.running_platform.entity.Post.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "run.id", target = "runId")
    @Mapping(source = "challenge.id", target = "challengeId")
    PostResponse toDto(Posts post);

}
