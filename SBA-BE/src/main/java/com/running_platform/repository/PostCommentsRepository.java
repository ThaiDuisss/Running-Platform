package com.running_platform.repository;

import com.running_platform.entity.Post.PostComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostCommentsRepository extends JpaRepository<PostComments, Long> {
    List<PostComments> findByPostId(Long postId);
    long countByPostId(Long postId);

}
