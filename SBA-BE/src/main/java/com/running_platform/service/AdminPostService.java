package com.running_platform.service;

import com.running_platform.dto.response.AdminPostResponse;
import com.running_platform.entity.Post.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminPostService {
    Page<AdminPostResponse> getAllPosts(Pageable pageable);
    AdminPostResponse getPostById(Long id);
    AdminPostResponse approvePost(Long id);
    AdminPostResponse rejectPost(Long id);
    AdminPostResponse deletePost(Long id);
}
