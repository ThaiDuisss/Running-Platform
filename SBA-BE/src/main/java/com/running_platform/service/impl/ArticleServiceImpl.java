package com.running_platform.service.impl;

import com.running_platform.constant.ErrorEnum;
import com.running_platform.dto.request.ArticleRequest;
import com.running_platform.dto.response.ArticleResponse;
import com.running_platform.entity.Post.Articles;
import com.running_platform.enums.CategoryEnum;
import com.running_platform.exception.AppException;
import com.running_platform.mapper.ArticleMapper;
import com.running_platform.repository.ArticleRepository;
import com.running_platform.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    @Override
    public Page<ArticleResponse> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(articleMapper::toResponse);
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(articleMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorEnum.ARTICLE_NOT_FOUND));
    }

    @Override
    public ArticleResponse addArticle(ArticleRequest articleRequest) {
        Articles article = articleMapper.toEntity(articleRequest);
        article.setPublishedAt(LocalDateTime.now());
        Articles savedArticle = articleRepository.save(article);
        return articleMapper.toResponse(savedArticle);
    }

    @Override
    public ArticleResponse updateArticle(Long id, ArticleRequest request) {
        Articles article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCategory(CategoryEnum.fromString(request.getCategory()));
        article.setSummary(request.getSummary());
        article.setThumbnailUrl(request.getThumbnailUrl());

        Articles updatedArticle = articleRepository.save(article);
        return articleMapper.toResponse(updatedArticle);
    }

    @Override
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new AppException(ErrorEnum.ARTICLE_NOT_FOUND);
        }
        articleRepository.deleteById(id);
    }
}
