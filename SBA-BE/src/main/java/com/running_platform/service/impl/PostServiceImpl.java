package com.running_platform.service.impl;

import com.running_platform.dto.request.CreatePostRequest;
import com.running_platform.dto.response.PostResponse;
import com.running_platform.entity.Post.Posts;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.PostStatus;
import com.running_platform.mapper.PostMapper;
import com.running_platform.repository.PostRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postsRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    @Override
    public PostResponse createPost(CreatePostRequest request, Authentication authentication) {

        String username = authentication.getName();
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Posts post = Posts.builder()
                .user(user)
                .content(request.getContent())
                .visibility(request.getVisibility())
                .status(PostStatus.PENDING)
                .build();

        postsRepository.save(post);


        PostResponse response = postMapper.toDto(post);

        return response;
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long userId, Pageable pageable) {
        Page<Posts> posts = postsRepository.findByUser_Id(userId, pageable);
        return posts.map(postMapper::toDto);
    }
}
