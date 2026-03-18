package com.running_platform.repository;

import com.running_platform.entity.Post.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByUser_Id(Long userId, Pageable pageable);

    Posts findPostsById(Long postId);

    @Query(value = """
    SELECT p.*
    FROM posts p
    LEFT JOIN users u ON p.user_id = u.id
    LEFT JOIN run_activity r ON p.run_id = r.id
    WHERE p.status = 'APPROVE'
    ORDER BY p.created_at DESC""", nativeQuery = true)
    List<Posts> findApprovedPosts();
}

