package com.running_platform.service.impl;

import com.running_platform.dto.request.PostCommentRequest;
import com.running_platform.dto.response.PostCommentsResponse;
import com.running_platform.entity.Post.PostComments;
import com.running_platform.entity.Post.Posts;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.mapper.PostCommentsMapper;
import com.running_platform.repository.PostCommentsRepository;
import com.running_platform.repository.PostRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.PostCommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentsServiceImpl implements PostCommentsService {
    private final PostRepository postRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;
    private final PostCommentsMapper postCommentsMapper;


    @Override
    public PostCommentsResponse createComment(PostCommentRequest request, Authentication authentication) {
        String username = authentication.getName();
        Posts posts = postRepository.findById(request.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        PostComments parent = null;
        if (request.getParentCommentId() != null) {
            parent = postCommentsRepository.findById(request.getParentCommentId())
                    .orElseThrow();
        }

        PostComments postComments = PostComments.builder()
                .content(request.getContent())
                .post(posts)
                .user(user)
                .parentComment(parent)
                .build();
        postComments.setCreatDate(Instant.now());

        postCommentsRepository.save(postComments);
        return postCommentsMapper.toDto(postComments);
    }

    @Override
    public List<PostCommentsResponse> getCommentsByPost(Long postId) {

        List<PostComments> allComments = postCommentsRepository.findByPostId(postId);

        Map<Long, List<PostComments>> map = allComments.stream()
                .filter(c -> c.getParentComment() != null)
                .collect(Collectors.groupingBy(c -> c.getParentComment().getId()));

        return allComments.stream()
                .filter(c -> c.getParentComment() == null)
                .map(c -> buildTreeOptimized(c, map))
                .toList();
    }

    @Override
    public long countCommentFromPost(Long postId) {
        return postCommentsRepository.countByPostId(postId);
    }



    private PostCommentsResponse buildTreeOptimized(
            PostComments comment,
            Map<Long, List<PostComments>> map
    ) {
        PostCommentsResponse dto = postCommentsMapper.toDto(comment);

        List<PostComments> children = map.get(comment.getId());

        if (children != null) {
            dto.setReplies(
                    children.stream()
                            .map(child -> buildTreeOptimized(child, map))
                            .toList()
            );
        }

        return dto;
        
    }
}
