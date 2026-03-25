package com.running_platform.service.impl;

import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.Post.PostReactions;
import com.running_platform.entity.Post.Posts;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ReactionType;
import com.running_platform.mapper.PostCommentsMapper;
import com.running_platform.mapper.PostReactionMapper;
import com.running_platform.repository.PostReactionRepository;
import com.running_platform.repository.PostRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.PostReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReactionsServiceImpl implements PostReactionsService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final PostReactionMapper postReactionMapper;

    @Override
    public PostReactionResponse reactPost(Long userId, Long postId, ReactionType type) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostReactions existing = postReactionRepository
                .findByUserIdAndPostId(userId, postId)
                .orElse(null);

        PostReactions finalReaction;

        if (existing == null) {
            PostReactions newReaction = PostReactions.builder()
                    .user(user)
                    .post(post)
                    .reaction(type)
                    .build();
            finalReaction = postReactionRepository.save(newReaction);
        } else {
            if (existing.getReaction() == type) {
                postReactionRepository.delete(existing);
                return null;

            } else {
                existing.setReaction(type);
                finalReaction = postReactionRepository.save(existing);
            }
        }
        return postReactionMapper.toDto(finalReaction);
    }

    @Override
    public long countReactFromPost(Long postId) {
        return postReactionRepository.countByPostId(postId);
    }

    @Override
    public Page<PostReactionResponse> getReactionsByPostId(Long postId, int page, int size) {

        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Bài viết không tồn tại!");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("creatDate").descending());
        return postReactionRepository.findReactionsByPostId(postId, pageable);
    }
}
