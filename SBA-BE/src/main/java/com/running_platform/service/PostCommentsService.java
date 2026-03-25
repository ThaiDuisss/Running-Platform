package com.running_platform.service;

import com.running_platform.dto.request.PostCommentRequest;
import com.running_platform.dto.response.PostCommentsResponse;
import com.running_platform.entity.Post.Posts;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostCommentsService {
    PostCommentsResponse createComment(PostCommentRequest request, Authentication authentication);
    List<PostCommentsResponse> getCommentsByPost(Long postId);
    long countCommentFromPost(Long postId);

}
