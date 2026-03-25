package com.running_platform.repository;

import com.running_platform.dto.response.PostReactionResponse;
import com.running_platform.entity.Post.PostReactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PostReactionRepository extends JpaRepository<PostReactions, Long> {
    Optional<PostReactions> findByUserIdAndPostId(Long userId, Long postId);
    long countByPostId(Long postId);

    @Query("""
        SELECT new com.running_platform.dto.response.PostReactionResponse(
            u.username, 
            p.id, 
            pr.reaction, 
            pr.creatDate, 
            pr.isDeleted
        )
        FROM PostReactions pr
        JOIN pr.user u
        JOIN pr.post p
        WHERE p.id = :postId 
          AND pr.isDeleted = false 
        ORDER BY pr.creatDate DESC""")
    Page<PostReactionResponse> findReactionsByPostId(@Param("postId") Long postId, Pageable pageable);
}
