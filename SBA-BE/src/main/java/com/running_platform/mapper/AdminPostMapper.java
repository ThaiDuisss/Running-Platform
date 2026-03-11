package com.running_platform.mapper;

import com.running_platform.dto.response.AdminPostResponse;
import com.running_platform.entity.Post.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminPostMapper {

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "run.id", target = "runId")
    @Mapping(source = "challenge.id", target = "challengeId")
    AdminPostResponse toDto(Posts post);
}