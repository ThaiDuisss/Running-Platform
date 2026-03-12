package com.running_platform.mapper;

import com.running_platform.dto.request.ArticleRequest;
import com.running_platform.dto.response.ArticleResponse;
import com.running_platform.entity.Post.Articles;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleResponse toResponse (Articles articles);
    Articles toEntity(ArticleRequest articleRequest);
}
