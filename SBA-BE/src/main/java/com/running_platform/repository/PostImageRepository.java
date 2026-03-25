package com.running_platform.repository;

import com.running_platform.entity.Post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long postId);
    List<PostImage> findByPostIdIn(List<Long> postIds);
}
