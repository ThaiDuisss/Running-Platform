package com.running_platform.repository;

import com.running_platform.entity.Post.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    Page<Posts> findByUser_Id(Long userId, Pageable pageable);
}

