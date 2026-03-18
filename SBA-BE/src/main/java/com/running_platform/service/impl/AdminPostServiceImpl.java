package com.running_platform.service.impl;

import com.running_platform.dto.response.AdminPostResponse;
import com.running_platform.entity.Post.Posts;
import com.running_platform.enums.PostStatus;
import com.running_platform.mapper.AdminPostMapper;
import com.running_platform.repository.PostRepository;
import com.running_platform.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {
    private final PostRepository postRepository;
    private final AdminPostMapper adminPostMapper;

    @Override
    public Page<AdminPostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(adminPostMapper::toDto);
    }

    @Override
    public AdminPostResponse getPostById(Long postId){
        Posts post = postRepository.findPostsById(postId);

        return adminPostMapper.toDto(post);
    }

    @Override
    public AdminPostResponse approvePost(Long id) {
        Posts post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus(PostStatus.APPROVE);
        postRepository.save(post);
        return adminPostMapper.toDto(post);
    }

    @Override
    public AdminPostResponse rejectPost(Long id) {
        Posts post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus(PostStatus.REJECT);
        postRepository.save(post);
        return adminPostMapper.toDto(post);
    }

    @Override
    public AdminPostResponse deletePost(Long id) {
        Posts post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setIsDeleted(true);
        postRepository.save(post);
        return adminPostMapper.toDto(post);
    }

}
