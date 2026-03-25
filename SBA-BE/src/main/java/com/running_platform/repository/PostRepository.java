package com.running_platform.repository;

import com.running_platform.dto.response.PostResponse;
import com.running_platform.entity.Post.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByUser_Id(Long userId, Pageable pageable);

    Posts findPostsById(Long postId);

    @Query("""
    SELECT new com.running_platform.dto.response.PostResponse(
        p.id, 
        u.username, 
        p.content,
        p.status, 
        p.creatDate,
        (SELECT COUNT(pr) FROM PostReactions pr WHERE pr.post.id = p.id),
        (SELECT pr.reaction FROM PostReactions pr WHERE pr.post.id = p.id AND pr.user.id = :userId)
    )
    FROM Posts p 
    LEFT JOIN p.user u 
    WHERE p.status = 'APPROVE'
    ORDER BY p.creatDate DESC""")
    Slice<PostResponse> findApprovedPostsFeed(@Param("userId") Long userId, Pageable pageable);
}

