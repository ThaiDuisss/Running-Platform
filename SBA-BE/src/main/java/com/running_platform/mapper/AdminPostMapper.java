package com.running_platform.mapper;

import com.running_platform.dto.response.AdminPostResponse;
import com.running_platform.entity.Post.PostImage;
import com.running_platform.entity.Post.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminPostMapper {

    @Mapping(source = "user.username", target = "username")
    AdminPostResponse toDto(Posts post);

    default List<String> map(List<PostImage> images) {
        if (images == null) return null;
        return images.stream()
                .map(PostImage::getImageUrl)
                .toList();
    }
}