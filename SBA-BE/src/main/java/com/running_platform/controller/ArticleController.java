package com.running_platform.controller;

import com.running_platform.dto.request.ArticleRequest;
import com.running_platform.dto.response.ArticleResponse;
import com.running_platform.service.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
@Tag(name = "Admin Manage Posts")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public Page<ArticleResponse> getArticles(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "0") Integer page) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        return articleService.getArticles(pageable);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ArticleResponse createArticle(@Valid @RequestBody ArticleRequest request) {
        return articleService.addArticle(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ArticleResponse updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleRequest request
    ) {
        return articleService.updateArticle(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}
