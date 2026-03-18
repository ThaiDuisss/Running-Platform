package com.running_platform.service;

import com.running_platform.dto.request.CreatePostRequest;
import com.running_platform.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(CreatePostRequest request, Authentication authentication);

    Page<PostResponse> getPostsByUser(Long userId, Pageable pageable);

    List<PostResponse> findAllApprovePost();
}
