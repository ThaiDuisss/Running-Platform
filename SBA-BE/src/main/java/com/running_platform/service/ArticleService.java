package com.running_platform.service;

import com.running_platform.dto.request.ArticleRequest;
import com.running_platform.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<ArticleResponse> getArticles(Pageable pageable);

    ArticleResponse getArticleById(Long id);

    ArticleResponse addArticle(ArticleRequest articleRequest);

    ArticleResponse updateArticle(Long id, ArticleRequest articleRequest);

    void deleteArticle(Long id);

}
