package com.running_platform.service;

import com.running_platform.dto.request.CreatePostRequest;
import com.running_platform.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface PostService {
    PostResponse createPost(CreatePostRequest request, Authentication authentication);

    Page<PostResponse> getPostsByUser(Long userId, Pageable pageable);
}
