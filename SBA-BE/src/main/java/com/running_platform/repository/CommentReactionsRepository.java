package com.running_platform.repository;

import com.running_platform.dto.response.CommentReactionResponse;
import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.Post.CommentReactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CommentReactionsRepository extends JpaRepository<CommentReactions, Long> {
    Optional<CommentReactions> findByCommentIdAndUserId(Long commentId, Long userId);
    long countByCommentId(Long commentId);
    @Query("""
        SELECT new com.running_platform.dto.response.CommentReactionResponse(
            u.username, 
            c.id, 
            cr.reactionType, 
            cr.creatDate, 
            cr.isDeleted
        )
        FROM CommentReactions cr
        JOIN cr.user u
        JOIN cr.comment c
        WHERE c.id = :commentId 
          AND cr.isDeleted = false 
        ORDER BY cr.creatDate DESC""")
    Page<CommentReactionResponse> findReactionsByCommentId(@Param("commentId") Long postId, Pageable pageable);
}
