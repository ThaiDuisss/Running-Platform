package com.running_platform.service.impl;

import com.running_platform.dto.response.CommentReactionResponse;
import com.running_platform.entity.Post.CommentReactions;
import com.running_platform.entity.Post.PostComments;
import com.running_platform.entity.UserAuth.Users;
import com.running_platform.enums.ReactionType;
import com.running_platform.mapper.CommentReactionMapper;
import com.running_platform.repository.CommentReactionsRepository;
import com.running_platform.repository.PostCommentsRepository;
import com.running_platform.repository.UserRepository;
import com.running_platform.service.CommentReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentReactionsServiceImpl implements CommentReactionsService {
    private final UserRepository userRepository;
    private final CommentReactionsRepository commentReactionsRepository;
    private final PostCommentsRepository commentRepository;
    private final CommentReactionMapper commentReactionMapper;
    @Override
    public CommentReactionResponse reactToComment(Long commentId, Long userId, ReactionType type) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostComments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        CommentReactions existing = commentReactionsRepository
                .findByCommentIdAndUserId(commentId, userId)
                .orElse(null);

        CommentReactions finalReaction;

        if (existing == null) {
            CommentReactions newReaction = CommentReactions.builder()
                    .user(user)
                    .comment(comment)
                    .reactionType(type)
                    .build();
            finalReaction = commentReactionsRepository.save(newReaction);
        } else {
            if (existing.getReactionType() == type) {
                commentReactionsRepository.delete(existing);
                return null;

            } else {
                existing.setReactionType(type);
                finalReaction = commentReactionsRepository.save(existing);
            }
        }
        return commentReactionMapper.toDto(finalReaction);
    }

    @Override
    public long countReactFromComment(Long commentId) {
        return commentReactionsRepository.countByCommentId(commentId);
    }

    @Override
    public Page<CommentReactionResponse> getReactionsByCommentId(Long commentId, int page, int size) {
        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Binh luan không tồn tại!");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("creatDate").descending());
        return commentReactionsRepository.findReactionsByCommentId(commentId, pageable);
    }
}
